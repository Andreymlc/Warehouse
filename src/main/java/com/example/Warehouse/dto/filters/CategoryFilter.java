package com.example.Warehouse.dto.filters;

public record CategoryFilter(
    String substring,
    boolean deleted
) {
}
