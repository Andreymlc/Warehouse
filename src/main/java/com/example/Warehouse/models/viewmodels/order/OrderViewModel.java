package com.example.Warehouse.models.viewmodels.order;

import java.time.LocalDateTime;

public record OrderViewModel(
    String number,
    Float totalPrice,
    LocalDateTime date
) {}
