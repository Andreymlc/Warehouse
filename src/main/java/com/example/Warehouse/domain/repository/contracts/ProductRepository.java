package com.example.Warehouse.domain.repository.contracts;

import com.example.Warehouse.domain.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {
    Product saveProduct(Product product);
    void deleteById(String id);

    Optional<Product> findById(String id);
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByCategoryName(String category, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategoryNameAndNameContainingIgnoreCase(String category, String name, Pageable pageable);

    Page<Product> findByStocksWarehouseId(String id, Pageable pageable);
    Page<Product> findByStocksWarehouseIdAndCategoryName(String id, String category, Pageable pageable);
    Page<Product> findByStocksWarehouseIdAndNameContainingIgnoreCase(String id, String name, Pageable pageable);
}
