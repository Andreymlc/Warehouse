package com.example.Warehouse.domain.repositories.contracts.user;

import com.example.Warehouse.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BaseUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String name);

    @Query("SELECT u.points FROM User u WHERE u.id = :id")
    int findPointsCount(@Param("id") String userId);
}
