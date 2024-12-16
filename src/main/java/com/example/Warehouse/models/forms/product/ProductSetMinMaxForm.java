package com.example.Warehouse.models.forms.product;

import com.example.Warehouse.models.forms.base.PagesForm;
import jakarta.validation.constraints.Min;

public record ProductSetMinMaxForm(
    @Min(value = 0, message = "Запас должен быть положительным числом")
    Integer value,
    PagesForm pages,
    String productId,
    String warehouseId
) {
    public ProductSetMinMaxForm {
            pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
