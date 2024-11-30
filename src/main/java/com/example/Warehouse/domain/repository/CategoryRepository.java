package com.example.Warehouse.domain.repository;

import com.example.Warehouse.domain.models.Category;
import com.example.Warehouse.domain.models.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category> {
    List<Category> findAll();
    Optional<Category> findByName(String name);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
