package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.entities.Category;
import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.models.dto.category.CategoryAddDto;
import com.example.Warehouse.models.dto.category.CategoryDto;
import com.example.Warehouse.models.dto.category.CategorySearchDto;
import com.example.Warehouse.models.filters.CategoryFilter;
import com.example.Warehouse.services.contracts.CategoryService;
import com.example.Warehouse.utils.specifications.CategorySpec;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(
        ModelMapper modelMapper,
        CategoryRepository categoryRepo
    ) {
        this.modelMapper = modelMapper;
        this.categoryRepo = categoryRepo;
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public String create(CategoryAddDto categoryDto) {
        var existingCategory = categoryRepo.findByName(categoryDto.name());

        if (existingCategory.isPresent()) {
            if (existingCategory.get().getIsDeleted()) {
                existingCategory.get().setIsDeleted(false);
                existingCategory.get().setDiscount(1f);
                categoryRepo.save(existingCategory.get());

                return "";
            }

            throw new InvalidDataException("Категория с таким имененм уже существует");
        }

        //var cat = modelMapper.map(categoryDto, Category.class);

        return categoryRepo.save(new Category(categoryDto.name(), 1 - (float) categoryDto.discount() / 100, false)).getId();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"categories", "products", "stocks"}, allEntries = true)
    public void delete(String id) {
        var category = categoryRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        category.getProducts().forEach(p -> p.setIsDeleted(true));
        category.setIsDeleted(true);
        categoryRepo.save(category);
    }

    @Override
    @Cacheable(
        value = "categories",
        key = "#categoryDto.substring + '-' + #categoryDto.page + '-' + #categoryDto.size() + '-' + #categoryDto.returnDeleted"
    )
    public Page<CategoryDto> findCategories(CategorySearchDto categoryDto) {
        Pageable pageable = PageRequest
            .of(categoryDto.page() - 1, categoryDto.size(), Sort.by("name"));

        Page<Category> categoryPage = categoryRepo.findAllByFilter(
            CategorySpec.filter(
                new CategoryFilter(
                    categoryDto.substring(),
                    categoryDto.returnDeleted()
                )
            ),
            pageable
        );

        return categoryPage.map(c -> modelMapper.map(c, CategoryDto.class));
    }

    @Override
    @Cacheable("categories")
    public List<String> findAllNamesCategories(boolean returnDeleted) {
        if (returnDeleted) {
            return categoryRepo.findAll()
                .stream()
                .map(Category::getName)
                .toList();
        } else {
            return categoryRepo.findAll()
                .stream()
                .filter(c -> !c.getIsDeleted())
                .map(Category::getName)
                .toList();
        }
    }

    @Override
    @CacheEvict(value = {"categories", "products", "stocks"}, allEntries = true)
    public void edit(String id, String name, int discount) {
        var category = categoryRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        if (name != null) category.setName(name);
        category.setDiscount(1 - (float) discount / 100);

        categoryRepo.save(category);
    }
}
