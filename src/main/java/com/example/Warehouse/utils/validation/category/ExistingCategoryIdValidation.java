package com.example.Warehouse.utils.validation.category;

import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistingCategoryIdValidation implements ConstraintValidator<ExistingCategoryId, String> {
    private final CategoryRepository categoryRepo;

    public ExistingCategoryIdValidation(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return categoryRepo.findById(id).isPresent();
    }
}
