package com.example.Warehouse.domain.repositories.impl;

import com.example.Warehouse.domain.models.User;
import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import com.example.Warehouse.domain.repositories.contracts.user.BaseUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepository<BaseUserRepository> implements UserRepository {
    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public int findPointsCount(String id) {
        return repository.findPointsCount(id);
    }

    @Override
    public Optional<User> findByUsername(String name) {
        return repository.findByUserName(name);
    }

    @Override
    public User save(User product) {
        return repository.save(product);
    }
}
