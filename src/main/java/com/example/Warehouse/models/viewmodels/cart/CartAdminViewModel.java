package com.example.Warehouse.models.viewmodels.cart;

import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductInCartViewModel;

import java.util.List;

public record CartAdminViewModel(
    Float totalPrice,
    BasePagesViewModel base,
    List<ProductInCartViewModel> products
) {}
