package com.example.Warehouse.models.dto.purchase;

import com.example.Warehouse.models.dto.product.ProductCartDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record PurchaseDetails(
    String number,
    String status,
    Float totalPrice,
    Integer cashback,
    LocalDateTime date,
    Integer pointsSpent,
    List<ProductCartDto> products
) implements Serializable {
}
