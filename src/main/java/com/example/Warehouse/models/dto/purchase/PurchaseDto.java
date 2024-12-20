package com.example.Warehouse.models.dto.purchase;

import java.io.Serializable;
import java.time.LocalDateTime;

public record PurchaseDto(
    String number,
    String status,
    Integer cashback,
    Float totalPrice,
    LocalDateTime date
) implements Serializable {
}
