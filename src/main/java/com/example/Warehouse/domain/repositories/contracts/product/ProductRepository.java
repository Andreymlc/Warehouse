package com.example.Warehouse.domain.repositories.contracts.product;

import com.example.Warehouse.domain.models.Product;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface ProductRepository extends BaseSaveRepository<Product> {
    Optional<Product> findById(String id);

    Page<Product> findAllByFilter(Specification<Product> specification, Pageable pageable);
}
