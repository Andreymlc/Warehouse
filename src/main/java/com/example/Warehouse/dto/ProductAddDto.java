package com.example.Warehouse.dto;

import java.math.BigDecimal;

public record ProductAddDto(
    String name,
    String category,
    Float price
) {}
