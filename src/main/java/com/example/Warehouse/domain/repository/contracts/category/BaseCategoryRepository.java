package com.example.Warehouse.domain.repository.contracts.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseCategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByName(String name);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
