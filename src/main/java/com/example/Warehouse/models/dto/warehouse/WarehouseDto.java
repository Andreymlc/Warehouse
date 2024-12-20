package com.example.Warehouse.models.dto.warehouse;

import java.io.Serializable;

public record WarehouseDto(
    String id,
    String name,
    String location,
    boolean isDeleted
) implements Serializable {
}
