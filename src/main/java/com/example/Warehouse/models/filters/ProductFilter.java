package com.example.Warehouse.models.filters;

public record ProductFilter(
    String category,
    boolean deleted,
    String substring
) {}
