package com.example.Warehouse.domain.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.models.Category;
import com.example.Warehouse.domain.repository.contracts.category.BaseCategoryRepository;
import com.example.Warehouse.domain.repository.contracts.category.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl extends BaseRepository<BaseCategoryRepository> implements CategoryRepository {
    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Category save(Category category) {
        return repository.save(category);
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Category> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }
}
