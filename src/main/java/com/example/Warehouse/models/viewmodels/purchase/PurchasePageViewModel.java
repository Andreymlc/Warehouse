package com.example.Warehouse.models.viewmodels.purchase;

import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;

import java.util.List;

public record PurchasePageViewModel(
    BasePagesViewModel base,
    List<PurchaseViewModel> purchases
) {
}
