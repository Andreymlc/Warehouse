package com.example.Warehouse.services.impl;

import org.springframework.stereotype.Service;
import com.example.Warehouse.dto.LoginUserDto;
import com.example.Warehouse.domain.models.User;
import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.dto.ResponseUserDto;
import com.example.Warehouse.dto.RegisterUserDto;
import com.example.Warehouse.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.exceptions.UserAlreadyExistsException;
import com.example.Warehouse.domain.repository.contracts.user.UserRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseUserDto register(RegisterUserDto registerUserDto) {
        var existingUser = userRepo.findByUserName(registerUserDto.userName());

        if (existingUser.isPresent())
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");

        var role = registerUserDto.role() ? Roles.ADMIN : Roles.USER;
        var result = userRepo.save(
            new User(
                role,
                registerUserDto.email(),
                0,
                registerUserDto.userName(),
                new ArrayList<>(),
                registerUserDto.password()
            )
        );

        return new ResponseUserDto(result.getId(), result.getRole());
    }

    @Override
    public ResponseUserDto login(LoginUserDto loginUserDto) {
        var existingUser = userRepo.findByUserName(loginUserDto.userName())
                .orElseThrow(() ->
                    new EntityNotFoundException("Пользователь с именем " + loginUserDto.userName() + "не найден"));

        if (!existingUser.getPasswordHash().equals(loginUserDto.password())) {
            throw new InvalidDataException("Неправильное имя пользователя или пароль");
        }

        return new ResponseUserDto(existingUser.getId(), existingUser.getRole());
    }
}
