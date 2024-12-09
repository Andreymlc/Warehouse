package com.example.Warehouse.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.example.Warehouse.dto.CategoryDto;
import org.springframework.stereotype.Service;
import com.example.Warehouse.dto.CategoryAddDto;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import com.example.Warehouse.domain.models.Category;
import com.example.Warehouse.services.CategoryService;
import com.example.Warehouse.domain.repository.contracts.category.CategoryRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(ModelMapper modelMapper, CategoryRepository categoryRepo) {
        this.modelMapper = modelMapper;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public String addCategory(CategoryAddDto categoryDto) {
        return categoryRepo.save(modelMapper.map(categoryDto, Category.class)).getId();
    }

    @Override
    public void deleteCategory(String id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public Page<CategoryDto> findCategories(String substring, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));

        Page<Category> categoryPage = substring != null
                ? categoryRepo.findByNameContainingIgnoreCase(substring, pageable)
                : categoryRepo.findAll(pageable);

        return categoryPage.map(c -> modelMapper.map(c, CategoryDto.class));
    }

    @Override
    public List<String> findAllNamesCategories() {
        return  categoryRepo.findAll()
                .stream()
                .map(Category::getName)
                .toList();
    }

    @Override
    public void editCategory(String id, String name, int discount) {
        var category = categoryRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        var isChanged = false;

        if (name != null && !category.getName().equalsIgnoreCase(name)) {
            category.setName(name);
            isChanged = true;
        }
        if (category.getDiscount() != discount) {
            category.setDiscount(1 - (float) discount / 100);
            isChanged = true;
        }
        if (isChanged) {
            categoryRepo.save(category);
        }
    }
}
