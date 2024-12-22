package com.example.Warehouse.models.forms.warehouse;

import com.example.Warehouse.utils.validations.warehouse.UniqueWarehouseName;
import jakarta.validation.constraints.Size;

    public record WarehouseCreateForm(
    @Size(min = 2, message = "Минимальная длина названия - 2")
    @UniqueWarehouseName
    String name,

    @Size(min = 2, message = "Минимальная длина локации - 2")
    String location
) {
}
