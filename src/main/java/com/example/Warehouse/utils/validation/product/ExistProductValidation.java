package com.example.Warehouse.utils.validation.product;

import com.example.Warehouse.domain.repositories.contracts.product.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistProductValidation implements ConstraintValidator<ExistProduct, String> {
    private final ProductRepository productRepo;

    public ExistProductValidation(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return productRepo.findById(id).isPresent();
    }
}
