package com.example.Warehouse.dto;

public record AddStockDto(
    Integer quantity,
    String productId,
    String warehouseId,
    Integer minimumStock,
    Integer maximumStock
) {}
