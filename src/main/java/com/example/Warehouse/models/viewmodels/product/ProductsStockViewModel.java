package com.example.Warehouse.models.viewmodels.product;

import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;

import java.util.List;

public record ProductsStockViewModel(
    BasePagesViewModel base,
    List<String> categories,
    List<ProductStockViewModel> products
) {
}
