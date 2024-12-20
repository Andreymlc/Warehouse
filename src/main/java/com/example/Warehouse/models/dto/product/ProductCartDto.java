package com.example.Warehouse.models.dto.product;

import java.io.Serializable;

public record ProductCartDto(
    String id,
    String name,
    String category,
    Integer quantity,
    Float totalPrice
) implements Serializable {
}
