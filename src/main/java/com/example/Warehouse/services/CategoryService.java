package com.example.Warehouse.services;

import com.example.WarehouseContracts.dto.CategoryDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    List<String> getAllNameCategories();
    String addCategory(CategoryDto categoryDto);
    Page<CategoryDto> getCategories(String substring, int page, int size);
}
