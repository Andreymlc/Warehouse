package com.example.Warehouse.domain.repositories.contracts.user.roles;

import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.models.Role;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface BaseRoleRepository extends Repository<Role, String> {
    Optional<Role> findRoleByName(Roles role);
}
