package com.example.Warehouse.domain.repositories.contracts.category;

import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends BaseSaveRepository<Category> {
    List<Category> findAll();

    Page<Category> findAllExisting(Pageable pageable);

    Optional<Category> findById(String id);

    Page<Category> findAll(Pageable pageable);

    Optional<Category> findByName(String name);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
