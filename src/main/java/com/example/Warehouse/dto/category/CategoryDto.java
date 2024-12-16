package com.example.Warehouse.dto.category;

public record CategoryDto (
    String id,
    String name,
    Integer discount,
    boolean isDeleted
) {}
