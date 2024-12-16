package com.example.Warehouse.domain.repositories.contracts.category;

import com.example.Warehouse.domain.models.Category;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends BaseSaveRepository<Category> {
    List<Category> findAll();

    Optional<Category> findById(String id);

    Page<Category> findAllByFilter(Specification<Category> specification, Pageable pageable);

    Optional<Category> findByName(String name);
}
