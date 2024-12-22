package com.example.Warehouse.domain.repositories.contracts.warehouse;

import com.example.Warehouse.domain.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BaseWarehouseRepository extends JpaRepository<Warehouse, String>, JpaSpecificationExecutor<Warehouse> {
    Optional<Warehouse> findByName(String name);
}
