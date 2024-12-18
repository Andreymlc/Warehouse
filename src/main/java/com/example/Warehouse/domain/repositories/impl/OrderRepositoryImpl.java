package com.example.Warehouse.domain.repositories.impl;

import com.example.Warehouse.domain.models.Order;
import com.example.Warehouse.domain.repositories.contracts.order.BaseOrderRepository;
import com.example.Warehouse.domain.repositories.contracts.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepositoryImpl extends BaseRepository<BaseOrderRepository> implements OrderRepository {
    @Override
    public Optional<Order> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Order> findByNumber(String number) {
        return Optional.empty();
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Order> findByUserId(String userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }
}
