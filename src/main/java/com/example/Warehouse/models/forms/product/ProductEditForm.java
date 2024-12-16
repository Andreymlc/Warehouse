package com.example.Warehouse.models.forms.product;

import com.example.Warehouse.utils.validation.category.ExistingCategoryName;
import com.example.Warehouse.utils.validation.product.ExistProduct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ProductEditForm(
    //@ExistProduct
    String id,

    @Min(value = 1, message = "Цена должна быть больше или равна 1")
    float price,

    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 2, message = "Минимальная длина названия 2")
    String name,

    //@ExistingCategoryName
    String category
) {}
