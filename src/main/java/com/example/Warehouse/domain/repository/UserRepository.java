package com.example.Warehouse.domain.repository;

import com.example.WarehouseContracts.enums.Roles;
import com.example.Warehouse.domain.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUserName(String name);
    List<User> findByRole(Roles role);
}
