package com.example.Warehouse.models.dto.product;

import java.io.Serializable;

public record ProductStockDto(
    String id,
    String name,
    int quantity,
    int minStock,
    int maxStock,
    String category,
    boolean isDeleted
) implements Serializable {
}
