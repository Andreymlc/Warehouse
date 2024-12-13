package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.models.Stock;
import com.example.Warehouse.domain.models.Warehouse;
import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
import com.example.Warehouse.dto.AddStockDto;
import com.example.Warehouse.dto.OrderItemDto;
import com.example.Warehouse.dto.WarehouseAddDto;
import com.example.Warehouse.dto.WarehouseDto;
import com.example.Warehouse.services.StockService;
import com.example.Warehouse.services.WarehouseService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
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
    public Page<WarehouseDto> findWarehouses(String substring, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));

        Page<Warehouse> warehouses = substring != null
            ? warehouseRepo.findByNameContainingIgnoreCase(substring, pageable)
            : warehouseRepo.findAll(pageable);

        return warehouses.map(w ->
            new WarehouseDto(
                w.getId(),
                w.getName(),
                w.getLocation()
            )
        );
    }

    @Override
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
    public void delete(String warehouseId) {
        var warehouse = warehouseRepo.findById(warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Склад не найден"));

        warehouse.setDeleted(true);
        warehouseRepo.save(warehouse);
    }
}
