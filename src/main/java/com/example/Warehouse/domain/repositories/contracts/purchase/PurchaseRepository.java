package com.example.Warehouse.domain.repositories.contracts.purchase;

import com.example.Warehouse.domain.entities.Purchase;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PurchaseRepository extends BaseSaveRepository<Purchase> {
    Optional<Purchase> findByNumber(String number);

    Page<Purchase> findAll(Pageable pageable);

    Page<Purchase> findByUserName(String userId, Pageable pageable);
}
