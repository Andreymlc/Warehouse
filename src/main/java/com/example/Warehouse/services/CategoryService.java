package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.WarehouseContracts.dto.CategoryDto;
import com.example.WarehouseContracts.dto.CategoryAddDto;

import java.util.List;

public interface CategoryService {
    void deleteCategory(String id);
    void editCategory(String id, String name, int discount);
    String addCategory(CategoryAddDto categoryDto);
    void setDiscount(String id, int discount);

    List<String> findAllNameCategories();
    Page<CategoryDto> findCategories(String substring, int page, int size);
}
