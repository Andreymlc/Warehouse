package com.example.Warehouse.models.dto.product;

import java.io.Serializable;

public record ProductDto(
    String id,
    String name,
    Float price,
    Float oldPrice,
    String category,
    Integer quantity,
    boolean isDeleted
) implements Serializable {
}
