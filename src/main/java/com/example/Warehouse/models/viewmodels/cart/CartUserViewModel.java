package com.example.Warehouse.models.viewmodels.cart;

import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductInCartViewModel;

import java.util.List;

public record CartUserViewModel(
    Float totalPrice,
    Integer pointsCount,
    BasePagesViewModel base,
    List<ProductInCartViewModel> products
) {}
