package com.example.Warehouse.dto.product;

public record ProductDto(
    String id,
    String name,
    Float price,
    Float oldPrice,
    String category,
    Integer quantity,
    boolean isDeleted
) {
}
