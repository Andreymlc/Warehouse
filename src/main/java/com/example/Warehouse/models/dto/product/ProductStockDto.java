package com.example.Warehouse.models.dto.product;

public record ProductStockDto(
    String id,
    String name,
    int quantity,
    int minStock,
    int maxStock,
    String category,
    boolean isDeleted
) {
}
