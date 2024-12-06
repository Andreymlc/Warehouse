package com.example.Warehouse.domain.repository;

import com.example.Warehouse.domain.models.Stock;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface StockRepository extends BaseRepository<Stock> {
    Optional<Stock> findByProductIdAndWarehouseId(String productId, String warehouseId);
    Optional<Stock> findForUpdateByProductIdAndWarehouseId(String productId, String warehouseId);

    @Query("SELECT SUM(s.quantity) FROM Stock s WHERE s.product.id = :id")
    Optional<Integer> findQuantityByProductId(@Param("id") String id);

    @Query("SELECT s.quantity FROM Stock s WHERE s.product.id = :productId AND s.warehouse.id = :warehouseId")
    Optional<Integer> findProductQuantityByWarehouseId(
        @Param("productId") String productId,
        @Param("warehouseId") String warehouseId);
}
