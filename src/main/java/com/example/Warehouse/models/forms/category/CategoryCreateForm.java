package com.example.Warehouse.models.forms.category;

import com.example.Warehouse.utils.validations.category.UniqueCategoryName;
import jakarta.validation.constraints.*;

public record CategoryCreateForm(
    @Size(min = 2, message = "Минимальная длина названия 2")
    @UniqueCategoryName
    String name,

    @NotNull(message = "Укажите скидку")
    @Min(value = 0, message = "Скидка должна быть больше или равна 0")
    @Max(value = 100, message = "Скидка должна быть меньше или равна 100")
    Integer discount
) {}
