package com.example.Warehouse.services.impl;

import org.springframework.stereotype.Service;
import com.example.Warehouse.domain.models.Stock;
import com.example.Warehouse.services.StockService;
import com.example.WarehouseContracts.dto.AddStockDto;
import com.example.Warehouse.domain.repository.StockRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.Warehouse.domain.repository.ProductRepository;
import com.example.Warehouse.domain.repository.WarehouseRepository;

@Service
public class StockServiceImpl implements StockService {
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
    public int getProductQuantityByProductId(String productId) {
        return stockRepo.getQuantityByProductId(productId).orElseThrow();
    }

    @Override
    public int getProductQuantityByWarehouseId(String productId, String warehouseId) {
        return stockRepo.getProductQuantityByWarehouseId(productId, warehouseId).orElseThrow();
    }

    @Override
    @Transactional
    public void moveProduct(String productId, String warehouseId, String newWarehouseId, int quantity) {
        stockRepo.pick(quantity, productId, warehouseId);
        stockRepo.put(quantity, productId, newWarehouseId);
    }

    @Override
    public void addStock(AddStockDto addStockDto) {
        var existingProduct = productRepo.findById(addStockDto.productId()).orElseThrow();
        var existingWarehouse = warehouseRepo.findById(addStockDto.warehouseId()).orElseThrow();

        stockRepo.save(new Stock(
                addStockDto.quantity(),
                addStockDto.minimumStock(),
                existingProduct,
                existingWarehouse
            )
        );
    }

    @Override
    @Transactional
    public void setMinimum(int minimum, String productId, String warehouseId) {
        stockRepo.setMinimum(minimum, productId, warehouseId);
    }
}
