package com.example.Warehouse.services;

import com.example.Warehouse.dto.PurchaseDto;
import org.springframework.data.domain.Page;

public interface PurchaseService {
    void addPurchase(String userId, int pointsSpent);

    PurchaseDto findPurchaseByNumber(String number);

    Page<PurchaseDto> findPurchases(String userId, int page, int size);

    Page<PurchaseDto> findAllPurchases(int page, int size);

    void check(String purchaseNumber);

    void setCanceled(String purchaseNumber);
}
