package com.example.Warehouse.models.dto.category;

import java.io.Serializable;

public record CategoryDto(
    String id,
    String name,
    Integer discount,
    boolean isDeleted
) implements Serializable {
}
