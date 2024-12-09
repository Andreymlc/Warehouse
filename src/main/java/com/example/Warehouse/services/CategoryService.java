package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.Warehouse.dto.CategoryDto;
import com.example.Warehouse.dto.CategoryAddDto;

import java.util.List;

public interface CategoryService {
    void deleteCategory(String id);
    void editCategory(String id, String name, int discount);
    String addCategory(CategoryAddDto categoryDto);

    List<String> findAllNamesCategories();
    Page<CategoryDto> findCategories(String substring, int page, int size);
}
