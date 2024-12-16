package com.example.Warehouse.dto.cart;

import com.example.Warehouse.dto.product.ProductCartDto;

import java.util.List;

public record CartDto(
    Float totalPrice,
    List<ProductCartDto> products
) {}
