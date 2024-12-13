package com.example.Warehouse.domain.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.models.Stock;
import com.example.Warehouse.domain.repositories.contracts.stock.StockRepository;
import com.example.Warehouse.domain.repositories.contracts.stock.BaseStockRepository;

import java.util.Optional;

@Repository
public class StockRepositoryImpl extends BaseRepository<BaseStockRepository> implements StockRepository {
    @Override
    public Optional<Stock> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Page<Stock> findAllByFilter(Specification<Stock> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

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
