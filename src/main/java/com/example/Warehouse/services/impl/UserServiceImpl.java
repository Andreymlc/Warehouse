package com.example.Warehouse.services.impl;

import org.springframework.stereotype.Service;
import com.example.Warehouse.domain.models.User;
import com.example.WarehouseContracts.enums.Roles;
import com.example.Warehouse.services.UserService;
import com.example.WarehouseContracts.dto.LoginUserDto;
import com.example.WarehouseContracts.dto.ResponseUserDto;
import com.example.WarehouseContracts.dto.RegisterUserDto;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.exceptions.UserNotFoundException;
import com.example.Warehouse.domain.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseUserDto register(RegisterUserDto registerUserDto) {
        var role = registerUserDto.role() ? Roles.ADMIN : Roles.USER;
        var result = userRepository.save(new User(
                role,
                registerUserDto.email(),
                0,
                registerUserDto.userName(),
                registerUserDto.password()
                )
        );

        return new ResponseUserDto(result.getId(), result.getRole());
    }

    @Override
    public ResponseUserDto login(LoginUserDto loginUserDto) {
        var existingUser = userRepository.findByUserName(loginUserDto.userName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем " + loginUserDto.userName() + "не найден"));

        if (!existingUser.getPasswordHash().equals(loginUserDto.password())) {
            throw new InvalidDataException("Неправильное имя пользователя или пароль");
        }

        return new ResponseUserDto(existingUser.getId(), existingUser.getRole());
    }
}
