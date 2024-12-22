package com.example.Warehouse.models.forms.product;

import com.example.Warehouse.models.forms.base.PagesForm;
import com.example.Warehouse.utils.validations.warehouse.ExistingWarehouse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductSetMinMaxForm(
    @NotNull(message = "Укажите запас")
    @Min(value = 0, message = "Запас должен быть положительным числом")
    Integer value,
    PagesForm pages,

    @NotEmpty(message = "Укажите id продукта")
    String productId,

    @ExistingWarehouse
    String warehouseId
) {
    public ProductSetMinMaxForm {
            pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
