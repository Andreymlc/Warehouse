package com.example.Warehouse.domain.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.enums.Status;
import com.example.Warehouse.domain.models.Order;
import com.example.Warehouse.domain.repository.contracts.order.OrderRepository;
import com.example.Warehouse.domain.repository.contracts.order.BaseOrderRepository;

import java.util.Optional;

@Repository
public class OrderRepositoryImpl extends BaseRepository<BaseOrderRepository> implements OrderRepository {
    @Override
    public Optional<Order> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Order> findByStatus(Status status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    @Override
    public Order save(Order product) {
        return repository.save(product);
    }
}
