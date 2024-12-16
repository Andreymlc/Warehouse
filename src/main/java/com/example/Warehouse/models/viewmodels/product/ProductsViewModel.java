package com.example.Warehouse.models.viewmodels.product;

import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;

import java.util.List;

public record ProductsViewModel(
    BasePagesViewModel base,
    List<String> categories,
    List<ProductViewModel> products
) {
}
