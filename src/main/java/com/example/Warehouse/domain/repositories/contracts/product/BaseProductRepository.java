package com.example.Warehouse.domain.repositories.contracts.product;

import com.example.Warehouse.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaseProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    @Query("""
        SELECT p
        FROM Product p
        JOIN PurchaseItem pi ON pi.product.id = p.id
        GROUP BY p
        ORDER BY SUM (pi.quantity) DESC
        LIMIT 5
        """)
    List<Product> findFiveMostPopular();
}
