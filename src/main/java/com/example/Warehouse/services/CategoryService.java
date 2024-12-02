package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.WarehouseContracts.dto.CategoryDto;
import com.example.WarehouseContracts.dto.CategoryAddDto;

import java.util.List;

public interface CategoryService {
    List<String> getAllNameCategories();
    List<CategoryDto> getAllCategories();
    void setDiscount(String id, int discount);
    String addCategory(CategoryAddDto categoryDto);
    Page<CategoryDto> getCategories(String substring, int page, int size);
}
