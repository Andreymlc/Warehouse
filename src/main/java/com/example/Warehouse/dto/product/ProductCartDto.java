package com.example.Warehouse.dto.product;

public record ProductCartDto(
    String id,
    String name,
    String category,
    Integer quantity,
    Float totalPrice
) {}
