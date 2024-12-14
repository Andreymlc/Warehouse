package com.example.Warehouse.services;

import com.example.Warehouse.dto.LoginUserDto;
import com.example.Warehouse.dto.RegisterUserDto;
import com.example.Warehouse.dto.ResponseUserDto;


public interface UserService {
    ResponseUserDto login(LoginUserDto loginUserDto);

    void register(RegisterUserDto registerUserDto);

    int getPointsCount(String userId);
}
