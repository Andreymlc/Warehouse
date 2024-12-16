package com.example.Warehouse.utils.validation.category;

import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistingCategoryNameValidation implements ConstraintValidator<ExistingCategoryName, String> {
    private final CategoryRepository categoryRepo;

    public ExistingCategoryNameValidation(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return categoryRepo.findByName(name).isPresent();
    }
}
