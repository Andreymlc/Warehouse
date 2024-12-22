package com.example.Warehouse.utils.validations.category;

import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.cache.annotation.Cacheable;

public class ExistingCategoryNameValidation implements ConstraintValidator<ExistingCategoryName, String> {
    private final CategoryRepository categoryRepo;

    public ExistingCategoryNameValidation(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    @Cacheable(value = "category", key = "#name")
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name != null &&  categoryRepo.findByName(name).isPresent();
    }
}
