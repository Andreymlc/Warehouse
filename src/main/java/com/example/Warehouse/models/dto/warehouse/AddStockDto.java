package com.example.Warehouse.models.dto.warehouse;

public record AddStockDto(
    Integer quantity,
    String productId,
    String warehouseId,
    Integer minimumStock,
    Integer maximumStock
) {}
