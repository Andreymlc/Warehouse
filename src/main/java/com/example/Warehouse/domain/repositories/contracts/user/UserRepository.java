package com.example.Warehouse.domain.repositories.contracts.user;

import com.example.Warehouse.domain.models.User;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;

import java.util.Optional;

public interface UserRepository extends BaseSaveRepository<User> {
    Optional<User> findById(String id);

    int findPointsCount(String id);

    Optional<User> findByUsername(String name);
}
