package com.example.Warehouse.domain.repository.contracts.user;

import com.example.Warehouse.domain.models.User;
import com.example.Warehouse.domain.repository.contracts.BaseSaveRepository;

import java.util.Optional;

public interface UserRepository extends BaseSaveRepository<User> {
    Optional<User> findById(String id);
    Optional<User> findByUserName(String name);
}
