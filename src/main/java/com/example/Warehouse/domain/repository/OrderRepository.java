package com.example.Warehouse.domain.repository;

import com.example.Warehouse.domain.enums.Status;
import com.example.Warehouse.domain.models.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends BaseRepository<Order> {
    List<Order> findByOrderDate(LocalDateTime date);
    List<Order> findByStatus(Status status);
}
