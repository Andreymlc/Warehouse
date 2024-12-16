package com.example.Warehouse.models.dto.category;

public record CategorySearchDto(
    int page,
    int size,
    String substring,
    boolean returnDeleted
) {
}
