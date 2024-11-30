package com.example.Warehouse.services;

import com.example.WarehouseContracts.dto.AddStockDto;

public interface StockService {
    String addStock(AddStockDto addStockDto);
    int getQuantityByProductId(String productId);
}
