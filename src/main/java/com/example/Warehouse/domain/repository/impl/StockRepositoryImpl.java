package com.example.Warehouse.domain.repository.impl;

import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.models.Stock;
import com.example.Warehouse.domain.repository.contracts.stock.StockRepository;
import com.example.Warehouse.domain.repository.contracts.stock.BaseStockRepository;

import java.util.Optional;

@Repository
public class StockRepositoryImpl extends BaseRepository<BaseStockRepository> implements StockRepository {
    @Override
    public Optional<Integer> findQuantityByProductId(String id) {
        return repository.findQuantityByProductId(id);
    }

    @Override
    public Optional<Stock> findByProductIdAndWarehouseId(String productId, String warehouseId) {
        return repository.findByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Override
    public Optional<Integer> findProductQuantityByWarehouseId(String productId, String warehouseId) {
        return repository.findProductQuantityByWarehouseId(productId, warehouseId);
    }

    @Override
    public Optional<Stock> findForUpdateByProductIdAndWarehouseId(String productId, String warehouseId) {
        return repository.findForUpdateByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Override
    public Stock save(Stock product) {
        return repository.save(product);
    }
}
