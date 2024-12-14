package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.models.Role;
import com.example.Warehouse.domain.models.User;
import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import com.example.Warehouse.domain.repositories.contracts.user.roles.RoleRepository;
import com.example.Warehouse.dto.LoginUserDto;
import com.example.Warehouse.dto.RegisterUserDto;
import com.example.Warehouse.dto.ResponseUserDto;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.exceptions.UserAlreadyExistsException;
import com.example.Warehouse.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
        UserRepository userRepo
        , RoleRepository roleRepo,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseUserDto register(RegisterUserDto registerUserDto) {
        if (!registerUserDto.password().equals(registerUserDto.confirmPassword())) {
            throw new RuntimeException("Пароли не совпадают");
        }

        var existingUser = userRepo.findByUsername(registerUserDto.userName());

        if (existingUser.isPresent())
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");

        var user = new User(
            registerUserDto.email(),
            0,
            registerUserDto.userName(),
            registerUserDto.password()
        );

        var role = registerUserDto.role() ? Roles.ADMIN : Roles.USER;
        user.setRoles(List.of(
            roleRepo.findRoleByName(role).orElseThrow(() -> new EntityNotFoundException("Роль не найдена"))
        ));

        userRepo.save(user);

        return new ResponseUserDto(result.getId(), result.getRoles());
    }

    @Override
    public int getPointsCount(String userId) {
        return userRepo.findPointsCount(userId);
    }

    @Override
    public ResponseUserDto login(LoginUserDto loginUserDto) {
        var existingUser = userRepo.findByUsername(loginUserDto.userName())
            .orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден"));

        if (!existingUser.getPasswordHash().equals(loginUserDto.password())) {
            throw new InvalidDataException("Неправильное имя пользователя или пароль");
        }

        return new ResponseUserDto(existingUser.getId(), existingUser.getRoles());
    }
}
