package com.example.Warehouse.models.forms.category;

import com.example.Warehouse.utils.validation.category.UniqueCategoryName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CategoryCreateForm(
    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 2, message = "Минимальная длина названия 2")
    @UniqueCategoryName
    String name,

    @Min(value = 0, message = "Скидка должна быть больше или равна 0")
    @Max(value = 100, message = "Скидка должна быть меньше или равна 100")
    Integer discount
) {}
