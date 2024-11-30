package com.example.Warehouse.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.example.Warehouse.domain.models.Category;
import com.example.WarehouseContracts.dto.CategoryDto;
import com.example.Warehouse.services.CategoryService;
import com.example.Warehouse.domain.repository.CategoryRepository;

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
    public List<CategoryDto> getAllCategories() {
        return  categoryRepo.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryDto.class))
                .toList();
    }

    public Page<CategoryDto> getCategories(String substring, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));
        Page<Category> categoryPage = substring != null
                ? categoryRepo.findByNameContainingIgnoreCase(substring, pageable)
                : categoryRepo.findAll(pageable);
        return categoryPage.map(c -> new CategoryDto(c.getName(), c.getDiscount()));
    }

    @Override
    public List<String> getAllNameCategories() {
        return  categoryRepo.findAll()
                .stream()
                .map(Category::getName)
                .toList();
    }

    @Override
    public String addCategory(CategoryDto categoryDto) {
        return categoryRepo.save(modelMapper.map(categoryDto, Category.class)).getId();
    }
}
