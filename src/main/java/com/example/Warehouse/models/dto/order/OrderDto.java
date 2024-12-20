package com.example.Warehouse.models.dto.order;

import java.io.Serializable;
import java.time.LocalDateTime;

public record OrderDto(
    String number,
    Float totalPrice,
    LocalDateTime date
) implements Serializable {
}
