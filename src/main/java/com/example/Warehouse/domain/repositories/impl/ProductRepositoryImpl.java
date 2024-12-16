package com.example.Warehouse.domain.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.entities.Product;
import com.example.Warehouse.domain.repositories.contracts.product.ProductRepository;
import com.example.Warehouse.domain.repositories.contracts.product.BaseProductRepository;

import java.util.Optional;

@Repository
public class ProductRepositoryImpl extends BaseRepository<BaseProductRepository> implements ProductRepository {
    @Override
    public Product save(Product product) {
        return repository.save(product);
    }
    @Override
    public Optional<Product> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Page<Product> findAllByFilter(Specification<Product> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }
}
