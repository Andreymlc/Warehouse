package com.example.Warehouse.utils.validations.warehouse;

import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.cache.annotation.Cacheable;

public class ExistingWarehouseValidation implements ConstraintValidator<ExistingWarehouse, String> {
    private final WarehouseRepository warehouseRepo;

    public ExistingWarehouseValidation(WarehouseRepository warehouseRepo) {
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    @Cacheable(value = "warehouse", key = "#id")
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return id != null && warehouseRepo.findById(id).isPresent();
    }
}
