package com.example.Warehouse.utils.validations.category;

import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.cache.annotation.Cacheable;

public class ExistingCategoryIdValidation implements ConstraintValidator<ExistingCategoryId, String> {
    private final CategoryRepository categoryRepo;

    public ExistingCategoryIdValidation(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    @Cacheable(value = "categoryId", key = "#id")
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return id != null &&  categoryRepo.findById(id).isPresent();
    }
}
