package com.example.Warehouse.domain.repository;

import com.example.Warehouse.domain.models.Product;

import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product> {
    Optional<Product> findByName(String name);
}
