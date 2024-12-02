package com.example.Warehouse.services;

import com.example.WarehouseContracts.dto.AddStockDto;

public interface StockService {
    void addStock(AddStockDto addStockDto);
    void setMinimum(int minimum, String productId, String warehouseId);
    int getProductQuantityByProductId(String productId);
    int getProductQuantityByWarehouseId(String productId, String warehouseId);
    void moveProduct(String productId, String warehouseId, String newWarehouseId, int quantity);
}
