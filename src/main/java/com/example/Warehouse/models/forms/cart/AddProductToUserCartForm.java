package com.example.Warehouse.models.forms.cart;

import com.example.Warehouse.utils.validations.product.ExistProduct;

public record AddProductToUserCartForm(
    @ExistProduct
    String productId
) {}
