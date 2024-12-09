package com.example.Warehouse.dto;

import com.example.Warehouse.domain.enums.Status;

import java.time.LocalDateTime;

public record OrderDto(
    String number,
    Status status,
    LocalDateTime date,
    Float totalPrice
) {}
