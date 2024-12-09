package com.example.Warehouse.domain.repository.contracts.stock;

import com.example.Warehouse.domain.models.Stock;
import org.springframework.data.repository.query.Param;
import com.example.Warehouse.domain.repository.contracts.BaseSaveRepository;

import java.util.Optional;

public interface StockRepository extends BaseSaveRepository<Stock> {
    Optional<Integer> findQuantityByProductId(@Param("id") String id);
    Optional<Stock> findByProductIdAndWarehouseId(String productId, String warehouseId);
    Optional<Integer> findProductQuantityByWarehouseId(String productId, String warehouseId);
    Optional<Stock> findForUpdateByProductIdAndWarehouseId(String productId, String warehouseId);
}
