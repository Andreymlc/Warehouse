package com.example.Warehouse.services;

import com.example.Warehouse.dto.ResponseUserDto;
import com.example.Warehouse.dto.LoginUserDto;
import com.example.Warehouse.dto.RegisterUserDto;


public interface UserService {
    ResponseUserDto login(LoginUserDto loginUserDto);
    ResponseUserDto register(RegisterUserDto registerUserDto);
}
