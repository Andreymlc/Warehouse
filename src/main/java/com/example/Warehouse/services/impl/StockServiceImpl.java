package com.example.Warehouse.services.impl;

import org.springframework.stereotype.Service;
import com.example.Warehouse.domain.models.Stock;
import jakarta.persistence.EntityNotFoundException;
import com.example.Warehouse.services.StockService;
import com.example.WarehouseContracts.dto.AddStockDto;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.domain.repository.StockRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.Warehouse.domain.repository.OldProductRepository;
import com.example.Warehouse.domain.repository.WarehouseRepository;

@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepo;
    private final OldProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;

    public StockServiceImpl(
        StockRepository stockRepo,
        OldProductRepository productRepo,
        WarehouseRepository warehouseRepo
    ) {
        this.stockRepo = stockRepo;
        this.productRepo = productRepo;
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    public void addStock(AddStockDto addStockDto) {
        var existingProduct = productRepo.findById(addStockDto.productId())
            .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        var existingWarehouse = warehouseRepo.findById(addStockDto.warehouseId())
            .orElseThrow(() -> new EntityNotFoundException("Склад не найден"));

        stockRepo.save(new Stock(
                addStockDto.quantity(),
                addStockDto.minimumStock(),
                addStockDto.maximumStock(),
                existingProduct,
                existingWarehouse
            )
        );
    }

    @Override
    public int getProductQuantityByProductId(String productId) {
        return stockRepo.findQuantityByProductId(productId).orElse(0);
    }

    @Override
    public int getProductQuantityByWarehouseId(String productId, String warehouseId) {
        return stockRepo.findProductQuantityByWarehouseId(productId, warehouseId).orElse(0);
    }

    @Override
    @Transactional
    public void moveProduct(String productId, String warehouseId, String newWarehouseId, int quantity) {
        var sourceStock = stockRepo.findForUpdateByProductIdAndWarehouseId(productId, warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Склад - источник с таким продуктом не найден"));

        var targetStock = stockRepo.findForUpdateByProductIdAndWarehouseId(productId, newWarehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Склад - конечный пункт, с таким продуктом не найден"));

        if (sourceStock.getQuantity() < quantity) {
            throw new InvalidDataException("На складе нет такого количества продукта");
        }

        sourceStock.setQuantity(sourceStock.getQuantity() - quantity);
        targetStock.setQuantity(targetStock.getQuantity() + quantity);

        stockRepo.save(sourceStock);
        stockRepo.save(targetStock);
    }

    @Override
    public void setMinimum(int minimum, String productId, String warehouseId) {
        var stock = stockRepo.findByProductIdAndWarehouseId(productId, warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Склад с таким продуктом не найден"));

        stock.setMinStock(minimum);

        stockRepo.save(stock);
    }

    @Override
    public void setMaximum(int maximum, String productId, String warehouseId) {
        var stock = stockRepo.findByProductIdAndWarehouseId(productId, warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Склад с таким продуктом не найден"));

        stock.setMaxStock(maximum);

        stockRepo.save(stock);
    }
}
