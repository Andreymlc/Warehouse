package com.example.Warehouse.domain.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.entities.Warehouse;
import com.example.Warehouse.domain.repositories.contracts.warehouse.BaseWarehouseRepository;
import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class WarehouseRepositoryImpl extends BaseRepository<BaseWarehouseRepository> implements WarehouseRepository {
    @Override
    public List<Warehouse> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Warehouse> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Warehouse> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Page<Warehouse> findAllByFilter(Specification<Warehouse> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    @Override
    public Warehouse save(Warehouse product) {
        return repository.save(product);
    }
}
