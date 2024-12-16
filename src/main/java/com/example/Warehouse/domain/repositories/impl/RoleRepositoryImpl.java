package com.example.Warehouse.domain.repositories.impl;

import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.entities.Role;
import com.example.Warehouse.domain.repositories.contracts.user.roles.RoleRepository;
import com.example.Warehouse.domain.repositories.contracts.user.roles.BaseRoleRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends BaseRepository<BaseRoleRepository> implements RoleRepository {

    @Override
    public Optional<Role> findRoleByName(Roles role) {
        return repository.findRoleByName(role);
    }

    public Role save(Role role) {
        return repository.save(role);
    }
}
