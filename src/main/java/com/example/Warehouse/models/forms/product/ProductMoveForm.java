package com.example.Warehouse.models.forms.product;

import com.example.Warehouse.models.forms.base.PagesForm;
import com.example.Warehouse.utils.validations.warehouse.ExistingWarehouse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductMoveForm(
    PagesForm pages,

    @NotEmpty(message = "Укажите id продукта")
    String productId,

    @NotNull(message = "Укажите количество продукта")
    @Min(value = 1, message = "Количество продукта должно быть больше 0")
    Integer quantityItems,

    @ExistingWarehouse
    String warehouseId,

    @ExistingWarehouse
    String newWarehouseId
) {
    public ProductMoveForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
