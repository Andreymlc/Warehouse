package com.example.Warehouse.models.forms.category;

import com.example.Warehouse.utils.validation.category.ExistingCategoryId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CategoryEditForm(
    //@ExistingCategoryId
    String id,
    String name,
    Integer discount
) {}
