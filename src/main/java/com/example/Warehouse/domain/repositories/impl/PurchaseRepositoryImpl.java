package com.example.Warehouse.domain.repositories.impl;

import com.example.Warehouse.domain.models.Purchase;
import com.example.Warehouse.domain.repositories.contracts.purchase.BasePurchaseRepository;
import com.example.Warehouse.domain.repositories.contracts.purchase.PurchaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PurchaseRepositoryImpl extends BaseRepository<BasePurchaseRepository> implements PurchaseRepository {
    @Override
    public Optional<Purchase> findByNumber(String number) {
        return repository.findByNumber(number);
    }

    @Override
    public Page<Purchase> findByUserName(String userId, Pageable pageable) {
        return repository.findByUserUsername(userId, pageable);
    }

    @Override
    public Page<Purchase> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Purchase save(Purchase purchase) {
        return repository.save(purchase);
    }
}
