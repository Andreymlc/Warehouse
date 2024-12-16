package com.example.Warehouse.models.filters;

public record StockFilter(
    String category,
    String substring,
    String warehouseId,
    boolean returnDeletedProduct
) {
}
