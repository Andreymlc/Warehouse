package com.example.Warehouse.services.contracts;

import com.example.Warehouse.dto.PageForRedis;
import com.example.Warehouse.dto.purchase.PurchaseDto;
import org.springframework.data.domain.Page;

public interface PurchaseService {
    void check(String purchaseNumber);

    void setCanceled(String purchaseNumber);

    PurchaseDto findPurchaseByNumber(String number);

    void addPurchase(String username, int pointsSpent);

    PageForRedis<PurchaseDto> findAllPurchases(int page, int size);

    PageForRedis<PurchaseDto> findPurchases(String username, int page, int size);
}
