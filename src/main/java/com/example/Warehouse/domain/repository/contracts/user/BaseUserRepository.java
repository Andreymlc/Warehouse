package com.example.Warehouse.domain.repository.contracts.user;

import com.example.Warehouse.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String name);
}
