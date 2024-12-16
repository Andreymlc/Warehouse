package com.example.Warehouse.domain.repositories.contracts.purchase;

import com.example.Warehouse.domain.models.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasePurchaseRepository extends JpaRepository<Purchase, String> {
    Optional<Purchase> findByNumber(String number);

    Page<Purchase> findByUserUsername(String number, Pageable pageable);
}
