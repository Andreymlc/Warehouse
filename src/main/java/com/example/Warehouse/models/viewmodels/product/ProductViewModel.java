package com.example.Warehouse.models.viewmodels.product;

public record ProductViewModel(
    String id,
    String name,
    Float price,
    Float oldPrice,
    String category,
    Integer quantity,
    boolean isDeleted
) {
}
