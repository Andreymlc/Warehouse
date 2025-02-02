package com.example.Warehouse.models.dto.product;

public record ProductSearchByWarehouseDto(
    int page,
    int size,
    String category,
    String substring,
    String warehouseId,
    boolean returnDeletedProduct
) {
}
