package com.example.Warehouse.domain.repository;

import com.example.Warehouse.domain.models.Stock;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

@Repository
public interface StockRepository extends BaseRepository<Stock> {
    @Query("SELECT SUM(s.quantity) FROM Stock s WHERE s.product.id = :id")
    Optional<Integer> getQuantityByProductId(@Param("id") String id);

    @Query("SELECT s.quantity FROM Stock s WHERE s.product.id = :productId AND s.warehouse.id = :warehouseId")
    Optional<Integer> getProductQuantityByWarehouseId(@Param("productId") String productId, @Param("warehouseId") String warehouseId);

    @Modifying
    @Query("UPDATE Stock s SET s.minimumStock = :minimum WHERE s.product.id = :productId AND s.warehouse.id = :warehouseId")
    int setMinimum(@Param("minimum") Integer minimum, @Param("productId") String productId, @Param("warehouseId") String warehouseId);

    @Modifying
    @Query("UPDATE Stock s SET s.quantity = s.quantity - :quantity WHERE s.product.id = :productId AND s.warehouse.id = :warehouseId")
    int pick(@Param("quantity") Integer quantity, @Param("productId") String productId, @Param("warehouseId") String warehouseId);

    @Modifying
    @Query("UPDATE Stock s SET s.quantity = s.quantity + :quantity WHERE s.product.id = :productId AND s.warehouse.id = :warehouseId")
    int put(@Param("quantity") Integer quantity, @Param("productId") String productId, @Param("warehouseId") String warehouseId);
}
