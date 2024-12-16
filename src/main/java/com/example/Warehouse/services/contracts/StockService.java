package com.example.Warehouse.services.contracts;

import com.example.Warehouse.dto.warehouse.AddStockDto;

public interface StockService {
    void addStock(AddStockDto addStockDto);

    int findProductQuantityByProductId(String productId);

    void setMinimum(int minimum, String productId, String warehouseId);

    void setMaximum(int maximum, String productId, String warehouseId);

    void decrStockQuantity(String warehouseId, String productId, int quantity);

    void incrStockQuantity(String warehouseId, String productId, int quantity);

    void moveProduct(String productId, String warehouseId, String newWarehouseId, int quantity);
}
