package com.example.Warehouse.models.dto.cart;

import com.example.Warehouse.models.dto.product.ProductCartDto;

import java.io.Serializable;
import java.util.List;

public record CartDto(
    Float totalPrice,
    List<ProductCartDto> products
) implements Serializable {
}
