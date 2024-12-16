package com.example.Warehouse.models.viewmodels.product;

public record ProductStockViewModel(
    String id,
    String name,
    int quantity,
    int minStock,
    int maxStock,
    String category,
    boolean isDeleted
) {
}
