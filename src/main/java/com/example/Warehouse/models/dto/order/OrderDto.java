package com.example.Warehouse.models.dto.order;

import java.time.LocalDateTime;

public record OrderDto(
    String number,
    Float totalPrice,
    LocalDateTime date
) {
}
