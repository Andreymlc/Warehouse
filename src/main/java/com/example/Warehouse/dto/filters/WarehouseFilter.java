package com.example.Warehouse.dto.filters;

public record WarehouseFilter(
    String substring,
    boolean returnDeleted
) {
}
