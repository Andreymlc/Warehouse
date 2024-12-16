package com.example.Warehouse.models.forms.product;

import com.example.Warehouse.models.forms.base.PagesForm;

public record ProductSearchForm(
    String category,
    PagesForm pages,
    String priceSort,
    boolean returnDeleted
) {
    public ProductSearchForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
        category = category == null || category.isBlank() || category.equals("Все") ? "" : category;
        priceSort = priceSort != null ? priceSort : "asc";
    }
}
