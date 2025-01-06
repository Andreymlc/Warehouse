package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.entities.Stock;
import com.example.Warehouse.domain.entities.Warehouse;
import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
import com.example.Warehouse.exceptions.WarehouseAlreadyExistsException;
import com.example.Warehouse.models.dto.order.OrderItemDto;
import com.example.Warehouse.models.dto.warehouse.AddStockDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseAddDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseSearchDto;
import com.example.Warehouse.models.filters.WarehouseFilter;
import com.example.Warehouse.services.contracts.StockService;
import com.example.Warehouse.services.contracts.WarehouseService;
import com.example.Warehouse.utils.specifications.WarehouseSpec;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final ModelMapper modelMapper;
    private final StockService stockService;
    private final WarehouseRepository warehouseRepo;

    private static final Logger LOG = LogManager.getLogger(Service.class);

    public WarehouseServiceImpl(
        ModelMapper modelMapper,
        StockServiceImpl stockService,
        WarehouseRepository warehouseRepo
    ) {
        this.modelMapper = modelMapper;
        this.stockService = stockService;
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    @Cacheable(value = "warehouses")
    public Page<WarehouseDto> findWarehouses(WarehouseSearchDto warehouseDto) {
        LOG.info("Cache not found. findWarehouses called, params: {}", warehouseDto);

        Pageable pageable = PageRequest
            .of(warehouseDto.page() - 1, warehouseDto.size(), Sort.by("name"));

        Page<Warehouse> warehouses = warehouseRepo.findAllByFilter(
            WarehouseSpec.filter(
                new WarehouseFilter(
                    warehouseDto.substring(),
                    warehouseDto.returnDeleted()
                )
            ),
            pageable
        );

        return warehouses.map(w ->
            new WarehouseDto(
                w.getId(),
                w.getName(),
                w.getLocation(),
                w.getIsDeleted()
            )
        );
    }

    @Override
    @CacheEvict(value = "warehouses", allEntries = true)
    public String add(WarehouseAddDto warehouseDto) {
        LOG.info("Cache 'warehouses' is cleared. addWarehouse called, params: {}", warehouseDto);

        return warehouseRepo.save(modelMapper.map(warehouseDto, Warehouse.class)).getId();
    }

    @Override
    public void fill(String warehouseId, List<OrderItemDto> items) {
        LOG.info("fillWarehouse called, params: warehouseId - {}, items - {}", warehouseId, items);

        var existingWarehouse = warehouseRepo.findById(warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Склад не найден"));

        var stocks = existingWarehouse.getStocks();

        Map<String, Integer> stoksMap = new HashMap<>();

        for (Stock stock : stocks) {
            stoksMap.put(stock.getProduct().getId(), stock.getQuantity());
        }

        items.forEach(i -> {
            if (stoksMap.containsKey(i.productId())) {
                stockService.incrStockQuantity(warehouseId, i.productId(), i.quantity());
            } else {
                stockService.addStock(
                    new AddStockDto(
                        i.quantity(),
                        i.productId(),
                        warehouseId,
                        0,
                        i.quantity()
                    )
                );
            }
        });
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "warehouse", key = "#warehouseId"),
        @CacheEvict(value = {"warehouses", "stocks", "products"}, allEntries = true)
    })
    public void delete(String warehouseId) {
        LOG.info("Cache 'warehouses' is cleared. deleteWarehouse called, warehouseId - {}", warehouseId);

        warehouseRepo.findById(warehouseId)
            .ifPresent(w -> {
                w.setIsDeleted(true);
                warehouseRepo.save(w);
            });
    }

    @Override
    @CacheEvict(value = "warehouses", allEntries = true)
    public void edit(String warehouseId, String name, String location) {
        LOG.info("Cache 'warehouses' is cleared. editWarehouse called, warehouseId - {}", warehouseId);

        var warehouse = warehouseRepo.findById(warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Склад не найден"));

        if (!warehouse.getName().equals(name) && warehouseRepo.findByName(name).isPresent()) {
            throw new WarehouseAlreadyExistsException();
        }

        warehouse.setName(name);
        warehouse.setLocation(location);

        warehouseRepo.save(warehouse);
    }
}
