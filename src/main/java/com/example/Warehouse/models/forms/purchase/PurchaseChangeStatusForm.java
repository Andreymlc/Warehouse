package com.example.Warehouse.models.forms.purchase;

import com.example.Warehouse.models.forms.base.PagesForm;

public record PurchaseChangeStatusForm(
    PagesForm pages,
    String purchaseNumber
) {
    public PurchaseChangeStatusForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
