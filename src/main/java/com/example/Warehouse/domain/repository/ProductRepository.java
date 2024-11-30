package com.example.Warehouse.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Product;

public interface ProductRepository extends BaseRepository<Product> {
    Page<Product> findByCategoryName(String category, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategoryNameAndNameContainingIgnoreCase(String category, String name, Pageable pageable);
}
