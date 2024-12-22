package com.example.Warehouse.models.dto.product;

public record ProductMoveDto(
    int quantity,
    String productId,
    String warehouseId,
    String newWarehouseI
) {
}
