package com.example.Warehouse.domain.repositories.contracts.stock;

import com.example.Warehouse.domain.entities.Stock;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends BaseSaveRepository<Stock> {
    Optional<Stock> findById(String id);

    Page<Stock> findAllByFilter(Specification<Stock> specification, Pageable pageable);

    Optional<Integer> findQuantityByProductId(String id);

    Optional<Stock> findByProductIdAndWarehouseId(String productId, String warehouseId);

    List<Stock> findByWarehouseId(String warehouseId);

    Optional<Stock> findForUpdateByProductIdAndWarehouseId(String productId, String warehouseId);
}
