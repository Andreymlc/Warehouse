package com.example.Warehouse.dto.warehouse;

public record WarehouseDto (
    String id,
    String name,
    String location,
    boolean isDeleted
) {}
