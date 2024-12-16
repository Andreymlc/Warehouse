package com.example.Warehouse.dto.purchase;

import java.time.LocalDateTime;

public record PurchaseDto(
    String number,
    String status,
    Integer cashback,
    Float totalPrice,
    LocalDateTime date
) {
}