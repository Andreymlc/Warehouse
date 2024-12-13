package com.example.Warehouse.services;

import com.example.Warehouse.dto.AddStockDto;

public interface StockService {
    void addStock(AddStockDto addStockDto);

    void incrStockQuantity(String warehouseId, String productId, int quantity);

    void decrStockQuantity(String warehouseId, String productId, int quantity);

    void setMinimum(int minimum, String productId, String warehouseId);

    void setMaximum(int maximum, String productId, String warehouseId);

    int findProductQuantityByProductId(String productId);

    int findProductQuantityByWarehouseId(String productId, String warehouseId);

    void moveProduct(String productId, String warehouseId, String newWarehouseId, int quantity);
}
