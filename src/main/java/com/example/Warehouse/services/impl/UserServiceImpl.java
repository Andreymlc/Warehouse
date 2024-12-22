package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.entities.User;
import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import com.example.Warehouse.domain.repositories.contracts.user.roles.RoleRepository;
import com.example.Warehouse.exceptions.UserAlreadyExistsException;
import com.example.Warehouse.models.dto.auth.RegisterUserDto;
import com.example.Warehouse.services.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl(
        UserRepository userRepo,
        RoleRepository roleRepo,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterUserDto registerUserDto) {
        LOG.info("register called, username - {}", registerUserDto.userName());

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
            passwordEncoder.encode(registerUserDto.password())
        );

        var role = registerUserDto.role() ? Roles.ADMIN : Roles.USER;
        user.setRoles(List.of(
            roleRepo.findRoleByName(role).orElseThrow(() -> new EntityNotFoundException("Роль не найдена"))
        ));

        userRepo.save(user);
    }

    @Override
    @Cacheable("points")
    public int getPointsCount(String username) {
        LOG.info("Cache not found. getPointsCount called, username - {}", username);

        return userRepo.findPointsCount(username);
    }
}
