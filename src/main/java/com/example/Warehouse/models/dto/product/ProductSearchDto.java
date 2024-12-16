package com.example.Warehouse.models.dto.product;

public record ProductSearchDto(
    int page,
    int size,
    String category,
    String substring,
    String priceSort,
    boolean isDeleted
) {
}
