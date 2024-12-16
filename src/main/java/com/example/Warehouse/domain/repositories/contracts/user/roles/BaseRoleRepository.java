package com.example.Warehouse.domain.repositories.contracts.user.roles;

import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseRoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByName(Roles role);
}
