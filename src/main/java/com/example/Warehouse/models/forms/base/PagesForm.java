package com.example.Warehouse.models.forms.base;

public record PagesForm(
    Integer page,
    Integer size,
    String substring
) {
    public PagesForm {
        page = page != null ? page : 1;
        size = size != null ? size : 12;
        substring = substring == null || substring.isBlank() ? "" : substring;
    }
}
