package com.example.Warehouse.models.dto.warehouse;

public record WarehouseDto (
    String id,
    String name,
    String location,
    boolean isDeleted
) {}
