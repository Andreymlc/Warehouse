package com.example.Warehouse.utils.validation.warehouse;

import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueWarehouseNameValidation implements ConstraintValidator<UniqueWarehouseName, String> {
    private final CategoryRepository categoryRepo;

    public UniqueWarehouseNameValidation(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return categoryRepo.findByName(name).isEmpty();
    }
}
