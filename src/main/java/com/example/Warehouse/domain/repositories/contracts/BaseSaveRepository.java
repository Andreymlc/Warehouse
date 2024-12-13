package com.example.Warehouse.domain.repositories.contracts;

public interface BaseSaveRepository<TEntity> {
    TEntity save(TEntity entity);
}
