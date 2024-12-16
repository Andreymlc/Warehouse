package com.example.Warehouse.services.contracts;

import com.example.Warehouse.dto.auth.RegisterUserDto;


public interface UserService {
    int getPointsCount(String username);

    void register(RegisterUserDto registerUserDto);
}
