package com.example.Warehouse.models.dto.auth;

public record RegisterUserDto(
    boolean role,
    String userName,
    String password,
    String confirmPassword
) {
}
