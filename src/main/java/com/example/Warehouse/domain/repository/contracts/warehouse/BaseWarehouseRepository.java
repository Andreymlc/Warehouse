package com.example.Warehouse.domain.repository.contracts.warehouse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseWarehouseRepository extends JpaRepository<Warehouse, String> {
    Page<Warehouse> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
