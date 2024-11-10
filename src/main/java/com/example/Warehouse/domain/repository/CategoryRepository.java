package com.example.Warehouse.domain.repository;

import com.example.Warehouse.domain.models.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category> {
    Optional<Category> findByName(String name);

    @Modifying
    @Transactional
    @Query("update Category c set c.discount = :discount where c.name = :categoryName")
    void updateDiscount(@Param("categoryName") String categoryName, @Param("discount") float discount);
}
