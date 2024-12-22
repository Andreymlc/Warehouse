package com.example.Warehouse.utils.validations.warehouse;

import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueWarehouseNameValidation implements ConstraintValidator<UniqueWarehouseName, String> {
    private final WarehouseRepository warehouseRepo;

    public UniqueWarehouseNameValidation(WarehouseRepository warehouseRepo) {
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name != null && warehouseRepo.findByName(name).isEmpty();
    }
}
