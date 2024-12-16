package com.example.Warehouse.dto.category;

public record CategorySearchDto(
    int page,
    int size,
    String substring,
    boolean returnDeleted
) {
}
