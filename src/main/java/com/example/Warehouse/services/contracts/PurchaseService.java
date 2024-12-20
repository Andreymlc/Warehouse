package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.purchase.PurchaseDto;
import org.springframework.data.domain.Page;

public interface PurchaseService {
    void check(String purchaseNumber);

    void setCanceled(String purchaseNumber);

    PurchaseDto findPurchaseByNumber(String number);

    void addPurchase(String username, int pointsSpent);

    Page<PurchaseDto> findAllPurchases(int page, int size);

    Page<PurchaseDto> findPurchases(String username, int page, int size);
}
