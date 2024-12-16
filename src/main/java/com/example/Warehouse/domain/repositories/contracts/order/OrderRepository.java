package com.example.Warehouse.domain.repositories.contracts.order;

import com.example.Warehouse.domain.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(String id);

    Optional<Order> findByNumber(String number);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByUsername(String username, Pageable pageable);
}
