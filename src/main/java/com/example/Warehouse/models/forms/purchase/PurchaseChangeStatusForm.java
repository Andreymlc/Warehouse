package com.example.Warehouse.models.forms.purchase;

import com.example.Warehouse.models.forms.base.PagesForm;
import jakarta.validation.constraints.NotNull;

public record PurchaseChangeStatusForm(
    PagesForm pages,

    @NotNull(message = "Укажите номер заказа")
    String purchaseNumber
) {
    public PurchaseChangeStatusForm {
        pages = pages == null ? new PagesForm(null, null, null) : pages;
    }
}
