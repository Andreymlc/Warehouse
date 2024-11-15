package com.example.Warehouse.domain.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<TEntity> extends Repository<TEntity, String> {
    void save(TEntity entity);
    Optional<TEntity> findById(String id);
    List<TEntity> findAll();
}
