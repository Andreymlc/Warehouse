package com.example.Warehouse.domain.repository;

import com.example.Warehouse.domain.models.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends BaseRepository<Warehouse> {
    Optional<Warehouse> findByName(String name);
}
