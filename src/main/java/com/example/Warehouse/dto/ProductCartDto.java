package com.example.Warehouse.dto;

public record ProductCartDto(
    String id,
    String name,
    String category,
    Integer quantity,
    Float totalPrice
) {}
