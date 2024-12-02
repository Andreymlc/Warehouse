package com.example.Warehouse.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.models.Warehouse;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends BaseRepository<Warehouse> {
    void deleteById(String id);
    Page<Warehouse>findByNameContainingIgnoreCase(String name, Pageable pageable);
}
