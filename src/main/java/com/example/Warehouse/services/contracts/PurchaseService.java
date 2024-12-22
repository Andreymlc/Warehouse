package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.purchase.PurchaseDetails;
import com.example.Warehouse.models.dto.purchase.PurchaseDto;
import org.springframework.data.domain.Page;

public interface PurchaseService {
    void check(String purchaseNumber);

    void setCanceled(String purchaseNumber);

    void addPurchase(String username, int pointsSpent);

    PurchaseDetails findPurchaseDetails(String number);

    Page<PurchaseDto> findAllPurchases(int page, int size);

    Page<PurchaseDto> findPurchases(String username, int page, int size);
}
