package com.example.Warehouse.models.viewmodels.category;

public record CategoryViewModel(
    String id,
    String name,
    Integer discount,
    boolean isDeleted
) {}
