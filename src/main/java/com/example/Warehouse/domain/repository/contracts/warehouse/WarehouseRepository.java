package com.example.Warehouse.domain.repository.contracts.warehouse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Warehouse;
import com.example.Warehouse.domain.repository.contracts.BaseSaveRepository;
import com.example.Warehouse.domain.repository.contracts.BaseDeleteRepository;

import java.util.Optional;

public interface WarehouseRepository extends BaseSaveRepository<Warehouse>, BaseDeleteRepository {
    Optional<Warehouse> findById(String id);
    Page<Warehouse> findAll(Pageable pageable);
    Page<Warehouse> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
