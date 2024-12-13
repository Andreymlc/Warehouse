package com.example.Warehouse.domain.repositories.contracts.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BaseProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Page<Product> findByCategoryName(String category, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategoryNameAndNameContainingIgnoreCase(String category, String name, Pageable pageable);

    Page<Product> findByStocksWarehouseId(String id, Pageable pageable);
    Page<Product> findByStocksWarehouseIdAndCategoryName(String id, String category, Pageable pageable);
    Page<Product> findByStocksWarehouseIdAndNameContainingIgnoreCase(String id, String name, Pageable pageable);
}
