package com.example.Warehouse.dto.filters;

public record StockFilter(
    String category,
    String substring,
    String warehouseId
) {
}
