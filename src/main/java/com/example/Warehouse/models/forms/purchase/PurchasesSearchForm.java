package com.example.Warehouse.models.forms.purchase;

import com.example.Warehouse.models.forms.base.PagesForm;

public record PurchasesSearchForm(
    PagesForm pages
) {
    public PurchasesSearchForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
