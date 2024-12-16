package com.example.Warehouse.models.forms.category;

import com.example.Warehouse.models.forms.base.PagesForm;

public record CategorySearchForm(
    PagesForm pages,
    boolean returnDeleted
) {
    public CategorySearchForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
