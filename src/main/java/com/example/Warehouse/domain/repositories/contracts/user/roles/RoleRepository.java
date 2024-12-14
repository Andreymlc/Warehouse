package com.example.Warehouse.domain.repositories.contracts.user.roles;

import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.models.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findRoleByName(Roles role);
}
