package com.example.Warehouse.dto.order;

public record OrderItemDto(
    String productId,
    Integer quantity
) {
}
