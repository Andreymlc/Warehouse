package com.example.Warehouse.models.forms.warehouse;

import com.example.Warehouse.utils.validations.warehouse.ExistingWarehouse;
import jakarta.validation.constraints.Size;

public record WarehouseEditForm(
    @Size(min = 2, message = "Минимальная длина названия 2")
    String name,

    @Size(min = 2, message = "Минимальная длина локации 2")
    String location,

    @ExistingWarehouse
    String warehouseId
) {
}
