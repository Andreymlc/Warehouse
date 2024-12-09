package com.example.Warehouse.dto;

import com.example.Warehouse.domain.enums.Roles;

public record ResponseUserDto(
    String id,
    Roles role
) {}
