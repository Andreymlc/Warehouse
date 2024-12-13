package com.example.Warehouse.domain.repositories.contracts.warehouse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Warehouse;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends BaseSaveRepository<Warehouse> {
    List<Warehouse> findAll();
    Optional<Warehouse> findById(String id);
    Page<Warehouse> findAll(Pageable pageable);
    Page<Warehouse> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
