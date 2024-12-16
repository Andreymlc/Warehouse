package com.example.Warehouse.domain.repositories.impl;

import com.example.Warehouse.domain.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.models.Category;
import com.example.Warehouse.domain.repositories.contracts.category.BaseCategoryRepository;
import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl extends BaseRepository<BaseCategoryRepository> implements CategoryRepository {

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
    public Page<Category> findAllByFilter(Specification<Category> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return repository.findByName(name);
    }
}
