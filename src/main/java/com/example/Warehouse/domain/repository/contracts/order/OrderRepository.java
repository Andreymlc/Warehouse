package com.example.Warehouse.domain.repository.contracts.order;

import com.example.Warehouse.domain.enums.Status;
import com.example.Warehouse.domain.models.Order;
import com.example.Warehouse.domain.repository.contracts.BaseSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepository extends BaseSaveRepository<Order> {
    Optional<Order> findById(String id);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByStatus(Status status, Pageable pageable);
}
