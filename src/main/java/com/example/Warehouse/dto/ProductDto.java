package com.example.Warehouse.dto;

public record ProductDto (
    String id,
    String name,
    String category,
    Float price,
    Integer quantity,
    Float oldPrice
) {}
