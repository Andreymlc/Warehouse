package com.example.Warehouse.services.impl;

import org.springframework.stereotype.Service;
import com.example.Warehouse.domain.models.Stock;
import com.example.WarehouseContracts.dto.AddStockDto;
import com.example.Warehouse.domain.repository.StockRepository;
import com.example.Warehouse.domain.repository.ProductRepository;
import com.example.Warehouse.domain.repository.WarehouseRepository;

@Service
public class StockServiceImpl implements com.example.Warehouse.services.StockService {
    private final StockRepository stockRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;

    public StockServiceImpl(
        StockRepository stockRepo,
        ProductRepository productRepo,
        WarehouseRepository warehouseRepo) {
        this.stockRepo = stockRepo;
        this.productRepo = productRepo;
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    public int getQuantityByProductId(String productId) {
        var quantity = stockRepo.getQuantityByProductId(productId).orElseThrow();
        return quantity;
    }

    @Override
    public String addStock(AddStockDto addStockDto) {
        var existingProduct = productRepo.findById(addStockDto.productId()).orElseThrow();
        var existingWarehouse = warehouseRepo.findById(addStockDto.warehouseId()).orElseThrow();

        return stockRepo.save(new Stock(
                addStockDto.quantity(),
                addStockDto.minimumStock(),
                existingProduct,
                existingWarehouse
            )
        ).getId();
    }
}
