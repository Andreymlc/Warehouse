package com.example.Warehouse.models.viewmodels.product;

public record ProductInCartViewModel(
    String id,
    String name,
    String category,
    Integer quantity,
    Float totalPrice
) {}
