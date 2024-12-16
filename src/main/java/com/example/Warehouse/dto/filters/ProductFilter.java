package com.example.Warehouse.dto.filters;

public record ProductFilter(
    String category,
    boolean deleted,
    String substring
) {}
