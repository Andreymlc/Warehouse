package com.example.Warehouse.models.dto.category;

public record CategoryDto (
    String id,
    String name,
    Integer discount,
    boolean isDeleted
) {}
