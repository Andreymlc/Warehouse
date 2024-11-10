package com.example.Warehouse.domain.repository;

import com.example.Warehouse.domain.enums.Status;
import com.example.Warehouse.domain.models.Purchase;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends BaseRepository<Purchase> {
    List<Purchase> findByPurchaseDate(LocalDateTime date);
    List<Purchase> findByStatus(Status status);
}
