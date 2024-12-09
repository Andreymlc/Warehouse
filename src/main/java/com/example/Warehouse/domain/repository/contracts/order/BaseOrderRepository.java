package com.example.Warehouse.domain.repository.contracts.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.enums.Status;
import com.example.Warehouse.domain.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseOrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByStatus(Status status, Pageable pageable);
}
