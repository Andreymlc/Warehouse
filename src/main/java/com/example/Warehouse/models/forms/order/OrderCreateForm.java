package com.example.Warehouse.models.forms.order;

import com.example.Warehouse.utils.validations.warehouse.ExistingWarehouse;

public record OrderCreateForm(
    @ExistingWarehouse
    String warehouseId
) {
}
