package com.example.Warehouse.domain.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.models.Warehouse;
import com.example.Warehouse.domain.repository.contracts.warehouse.BaseWarehouseRepository;
import com.example.Warehouse.domain.repository.contracts.warehouse.WarehouseRepository;

import java.util.Optional;

@Repository
public class WarehouseRepositoryImpl extends BaseRepository<BaseWarehouseRepository> implements WarehouseRepository {
    @Override
    public Optional<Warehouse> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Page<Warehouse> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Warehouse> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Warehouse save(Warehouse product) {
        return repository.save(product);
    }
}
