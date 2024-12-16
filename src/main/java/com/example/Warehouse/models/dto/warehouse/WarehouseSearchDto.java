package com.example.Warehouse.models.dto.warehouse;

public record WarehouseSearchDto(
    int page,
    int size,
    String substring,
    boolean returnDeleted
) {
}
