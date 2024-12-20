package com.example.Warehouse.domain.repositories.contracts.user;

import com.example.Warehouse.domain.entities.User;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;

import java.util.Optional;

public interface UserRepository extends BaseSaveRepository<User> {
    Optional<User> findById(String id);

    int findPointsCount(String username);

    Optional<User> findByUsername(String name);
}
