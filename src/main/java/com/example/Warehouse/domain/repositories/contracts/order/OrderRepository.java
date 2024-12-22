package com.example.Warehouse.domain.repositories.contracts.order;

import com.example.Warehouse.domain.entities.Order;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepository extends BaseSaveRepository<Order> {
    Optional<Order> findById(String id);

    Optional<Order> findByNumber(String number);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByUsername(String username, Pageable pageable);
}
