package com.example.Warehouse.domain.repositories.contracts.warehouse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.entities.Warehouse;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends BaseSaveRepository<Warehouse> {
    List<Warehouse> findAll();

    Optional<Warehouse> findById(String id);

    Optional<Warehouse> findByName(String name);

    Page<Warehouse> findAllByFilter(Specification<Warehouse> specification, Pageable pageable);
}
