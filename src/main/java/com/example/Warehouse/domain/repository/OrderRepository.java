package com.example.Warehouse.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Order;
import org.springframework.stereotype.Repository;
import com.example.WarehouseContracts.enums.Status;

@Repository
public interface OrderRepository extends BaseRepository<Order> {
    Page<Order> findByStatus(Status status, Pageable pageable);
}
