package com.example.Warehouse.models.viewmodels.order;

import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;

import java.util.List;

public record OrdersPageViewModel(
    BasePagesViewModel base,
    List<OrderViewModel> orders
) {
}
