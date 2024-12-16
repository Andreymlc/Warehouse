package com.example.Warehouse.models.viewmodels.warehouse;

public record WarehouseViewModel (
    String id,
    String name,
    String location,
    boolean isDeleted
) {}
