package com.example.Warehouse.dto.auth;

public record RegisterUserDto (
        String email,
        boolean role,
        String userName,
        String password,
        String confirmPassword
) {}
