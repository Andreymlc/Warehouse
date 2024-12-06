package com.example.Warehouse.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.models.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category> {
    Optional<Category> findByName(String name);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Modifying
    @Query("UPDATE Category c SET c.discount = :discount WHERE c.id = :id")
    void setDiscount(@Param("id") String id, @Param("discount") Float discount);

    void deleteById(String id);
}
