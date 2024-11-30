package com.example.Warehouse.domain.repository;

import com.example.Warehouse.domain.models.Stock;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface StockRepository extends BaseRepository<Stock> {
    @Query("SELECT SUM(s.quantity) FROM Stock s WHERE s.product.id = :id")
    Optional<Integer> getQuantityByProductId(@Param("id") String id);
}
