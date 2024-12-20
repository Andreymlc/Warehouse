package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.entities.Stock;
import com.example.Warehouse.domain.entities.Warehouse;
import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
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
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(
        value = "warehouses",
        key = "#warehouseDto.substring + '-' + #warehouseDto.size + '-' + #warehouseDto.page + '-' + #warehouseDto.returnDeleted"
    )
    public Page<WarehouseDto> findWarehouses(WarehouseSearchDto warehouseDto) {
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
        return warehouseRepo.save(modelMapper.map(warehouseDto, Warehouse.class)).getId();
    }

    public void fill(String warehouseId, List<OrderItemDto> items) {

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
    @CacheEvict(value = "warehouses", allEntries = true)
    public void delete(String warehouseId) {
        var warehouse = warehouseRepo.findById(warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Склад не найден"));

        warehouse.setIsDeleted(true);
        warehouseRepo.save(warehouse);
    }
}
