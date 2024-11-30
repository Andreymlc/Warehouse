package com.example.Warehouse.services;

import com.example.WarehouseContracts.dto.ResponseUserDto;
import com.example.WarehouseContracts.enums.Roles;
import com.example.WarehouseContracts.dto.LoginUserDto;
import com.example.WarehouseContracts.dto.RegisterUserDto;


public interface UserService {
    ResponseUserDto register(RegisterUserDto registerUserDto);
    ResponseUserDto login(LoginUserDto loginUserDto);
}
