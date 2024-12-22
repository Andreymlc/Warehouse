package com.example.Warehouse.utils.validations.product;

import com.example.Warehouse.domain.repositories.contracts.product.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.cache.annotation.Cacheable;

public class ExistProductValidation implements ConstraintValidator<ExistProduct, String> {
    private final ProductRepository productRepo;

    public ExistProductValidation(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return id != null &&  productRepo.findById(id).isPresent();
    }
}
