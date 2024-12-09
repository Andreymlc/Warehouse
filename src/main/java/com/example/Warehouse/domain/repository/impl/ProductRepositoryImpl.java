package com.example.Warehouse.domain.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.models.Product;
import com.example.Warehouse.domain.repository.contracts.product.ProductRepository;
import com.example.Warehouse.domain.repository.contracts.product.BaseProductRepository;

import java.util.Optional;

@Repository
public class ProductRepositoryImpl extends BaseRepository<BaseProductRepository> implements ProductRepository {
    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Product> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Product> findByCategoryName(String category, Pageable pageable) {
        return repository.findByCategoryName(category, pageable);
    }

    @Override
    public Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<Product> findByCategoryNameAndNameContainingIgnoreCase(String category, String name, Pageable pageable) {
        return repository.findByCategoryNameAndNameContainingIgnoreCase(category, name, pageable);
    }

    @Override
    public Page<Product> findByStocksWarehouseId(String id, Pageable pageable) {
        return repository.findByStocksWarehouseId(id, pageable);
    }

    @Override
    public Page<Product> findByStocksWarehouseIdAndCategoryName(String id, String category, Pageable pageable) {
        return repository.findByStocksWarehouseIdAndCategoryName(id, category, pageable);
    }

    @Override
    public Page<Product> findByStocksWarehouseIdAndNameContainingIgnoreCase(String id, String name, Pageable pageable) {
        return repository.findByStocksWarehouseIdAndNameContainingIgnoreCase(id, name, pageable);
    }
}
