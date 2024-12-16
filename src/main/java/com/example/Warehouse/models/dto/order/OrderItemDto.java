package com.example.Warehouse.models.dto.order;

public record OrderItemDto(
    String productId,
    Integer quantity
) {
}
