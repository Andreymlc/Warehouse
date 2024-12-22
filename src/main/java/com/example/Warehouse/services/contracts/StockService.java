package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.product.ProductMoveDto;
import com.example.Warehouse.models.dto.warehouse.AddStockDto;

public interface StockService {
    void addStock(AddStockDto addStockDto);

    void moveProduct(ProductMoveDto productDto);

    int findProductQuantityByProductId(String productId);

    void setMinimum(int minimum, String productId, String warehouseId);

    void setMaximum(int maximum, String productId, String warehouseId);

    void decrStockQuantity(String warehouseId, String productId, int quantity);

    void incrStockQuantity(String warehouseId, String productId, int quantity);
}
