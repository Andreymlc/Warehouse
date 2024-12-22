package com.example.Warehouse.models.dto.product;

public record ProductEditDto(
    String id,
    float price,
    String name,
    String categoryName
) {
}
