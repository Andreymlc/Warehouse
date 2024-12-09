package com.example.Warehouse.domain.repository.contracts;

public interface BaseSaveRepository<TEntity> {
    TEntity save(TEntity entity);
}
