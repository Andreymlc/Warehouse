package com.example.Warehouse.utils.validations.category;

import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueCategoryNameValidation implements ConstraintValidator<UniqueCategoryName, String> {
    private final CategoryRepository categoryRepo;

    public UniqueCategoryNameValidation(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name != null &&  categoryRepo.findByName(name).isEmpty();
    }
}
