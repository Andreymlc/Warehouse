package com.example.Warehouse.domain.repositories.contracts.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseOrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByNumber(String number, Pageable pageable);
    Page<Order> findByUserUsername(String username, Pageable pageable);
}
