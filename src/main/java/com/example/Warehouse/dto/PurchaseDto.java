package com.example.Warehouse.dto;

import java.time.LocalDateTime;

public record PurchaseDto(
    String number,
    String status,
    Integer cashback,
    Float totalPrice,
    LocalDateTime date
) {
}
