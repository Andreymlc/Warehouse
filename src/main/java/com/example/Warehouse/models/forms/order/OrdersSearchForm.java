package com.example.Warehouse.models.forms.order;

import com.example.Warehouse.models.forms.base.PagesForm;

public record OrdersSearchForm(
    PagesForm pages
) {
    public OrdersSearchForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
