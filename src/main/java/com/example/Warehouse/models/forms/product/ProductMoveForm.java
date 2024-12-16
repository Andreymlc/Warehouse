package com.example.Warehouse.models.forms.product;

import com.example.Warehouse.models.forms.base.PagesForm;

public record ProductMoveForm(
    PagesForm pages,
    String productId,
    Integer countItems,
    String warehouseId,
    String newWarehouseId
) {
    public ProductMoveForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
