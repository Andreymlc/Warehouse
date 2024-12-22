package com.example.Warehouse.domain.repositories.contracts.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseOrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByUserUsername(String username, Pageable pageable);
}
