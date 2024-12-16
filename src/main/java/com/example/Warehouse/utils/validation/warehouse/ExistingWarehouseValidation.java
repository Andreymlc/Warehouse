package com.example.Warehouse.utils.validation.warehouse;

import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistingWarehouseValidation implements ConstraintValidator<ExistingWarehouse, String> {
    private final WarehouseRepository warehouseRepo;

    public ExistingWarehouseValidation(WarehouseRepository warehouseRepo) {
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return warehouseRepo.findById(id).isPresent();
    }
}
