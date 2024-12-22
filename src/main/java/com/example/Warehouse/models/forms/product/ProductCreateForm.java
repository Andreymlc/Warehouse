package com.example.Warehouse.models.forms.product;

import com.example.Warehouse.utils.validations.category.ExistingCategoryName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreateForm(
    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 2, message = "Минимальная длина названия 2")
    String name,

    @NotNull(message = "Укажите цену")
    @Min(value = 1, message = "Цена должна быть больше или равна 1")
    Float price,

    @ExistingCategoryName
    String category
) {
}
