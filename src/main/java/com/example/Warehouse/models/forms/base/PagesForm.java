package com.example.Warehouse.models.forms.base;

import jakarta.validation.constraints.Min;

public record PagesForm(
    Integer page,
    Integer size,
    String substring
) {
    public PagesForm {
        page = page == null || page < 1 ? 1 : page;
        size = size == null || size < 1  ? 12 : size;
        substring = substring == null || substring.isBlank() ? "" : substring;
    }
}
