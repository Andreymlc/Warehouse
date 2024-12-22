package com.example.Warehouse.models.forms.purchase;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PurchaseCreateForm(
    @NotNull(message = "Укажите количество баллов")
    @Min(value = 0, message = "Количество баллво должно быть положительным числом или 0")
    Integer pointsSpent
) {
}
