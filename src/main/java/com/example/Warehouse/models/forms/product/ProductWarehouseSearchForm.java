package com.example.Warehouse.models.forms.product;

import com.example.Warehouse.models.forms.base.PagesForm;

public record ProductWarehouseSearchForm(
    String category,
    PagesForm pages,
    String warehouseId,
    boolean returnDeleted
) {
    public ProductWarehouseSearchForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
        category = category == null || category.isBlank() || category.equals("Все") ? "" : category;
    }
}
