package com.example.Warehouse.dto;

import java.time.LocalDateTime;

public record OrderDto(
    String number,
    Float totalPrice,
    LocalDateTime date
) {
}
