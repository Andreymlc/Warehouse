package com.example.Warehouse.models.filters;

public record WarehouseFilter(
    String substring,
    boolean returnDeleted
) {
}
