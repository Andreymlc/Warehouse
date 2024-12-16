package com.example.Warehouse.models.forms.warehouse;

import com.example.Warehouse.models.forms.base.PagesForm;

public record WarehousesSearchForm(
    PagesForm pages,
    boolean returnDeleted
) {
    public WarehousesSearchForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
