package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.PageForRedis;
import com.example.Warehouse.models.dto.purchase.PurchaseDto;

public interface PurchaseService {
    void check(String purchaseNumber);

    void setCanceled(String purchaseNumber);

    PurchaseDto findPurchaseByNumber(String number);

    void addPurchase(String username, int pointsSpent);

    PageForRedis<PurchaseDto> findAllPurchases(int page, int size);

    PageForRedis<PurchaseDto> findPurchases(String username, int page, int size);
}
