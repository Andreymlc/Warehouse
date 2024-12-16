package com.example.Warehouse.domain.repositories.contracts.product;

import com.example.Warehouse.domain.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BaseProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
}
