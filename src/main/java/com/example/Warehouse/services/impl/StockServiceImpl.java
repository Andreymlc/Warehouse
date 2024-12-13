package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.models.Stock;
import com.example.Warehouse.domain.repositories.contracts.product.ProductRepository;
import com.example.Warehouse.domain.repositories.contracts.stock.StockRepository;
import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
import com.example.Warehouse.dto.AddStockDto;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.services.StockService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;

    public StockServiceImpl(
        StockRepository stockRepo,
        ProductRepository productRepo,
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
    public int findProductQuantityByProductId(String productId) {
        return stockRepo.findQuantityByProductId(productId).orElse(0);
    }

    @Override
    public int findProductQuantityByWarehouseId(String productId, String warehouseId) {
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

    @Override
    public void incrStockQuantity(String warehouseId, String productId, int quantity) {
        var stock = stockRepo.findByProductIdAndWarehouseId(productId, warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Запас не найден"));

        if (stock.getMaxStock() >= stock.getQuantity() + quantity) {
            stock.setQuantity(stock.getQuantity() + quantity);
        } else {
            throw new InvalidDataException("На складе нет места");
        }

        stockRepo.save(stock);
    }

    @Override
    public void decrStockQuantity(String warehouseId, String productId, int quantity) {
        var stock = stockRepo.findByProductIdAndWarehouseId(productId, warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Запас не найден"));

        if (stock.getQuantity() - quantity >= 0) {
            stock.setQuantity(stock.getQuantity() - quantity);
        } else {
            throw new InvalidDataException("На складе нет столько товара");
        }

        stockRepo.save(stock);
    }
}
