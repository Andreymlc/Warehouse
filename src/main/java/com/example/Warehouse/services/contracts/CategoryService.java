package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.category.CategoryAddDto;
import com.example.Warehouse.models.dto.category.CategoryDto;
import com.example.Warehouse.models.dto.category.CategorySearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    void delete(String id);

    String create(CategoryAddDto categoryDto);

    void edit(String id, String name, int discount);

    List<String> findAllNamesCategories(boolean returnDeleted);

    Page<CategoryDto> findCategories(CategorySearchDto categoryDto);
}
