package com.example.Warehouse.domain.repositories.contracts.user.roles;

import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.entities.Role;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;

import java.util.Optional;

public interface RoleRepository extends BaseSaveRepository<Role> {
    Optional<Role> findRoleByName(Roles role);
}
