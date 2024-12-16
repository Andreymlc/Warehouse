package com.example.Warehouse.dto.warehouse;

public record WarehouseSearchDto(
    int page,
    int size,
    String substring,
    boolean returnDeleted
) {
}
