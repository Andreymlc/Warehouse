package com.example.Warehouse.services.contracts;

import com.example.Warehouse.dto.PageForRedis;
import com.example.Warehouse.dto.product.*;

public interface ProductService {
    void deleteProduct(String productId);

    String addProduct(ProductAddDto productAddDto);

    void editProduct(String id, String name, String categoryName, float price);

    PageForRedis<ProductDto> findProducts(ProductSearchDto productSearchDto);

    PageForRedis<ProductStockDto> findProductsByWarehouse(ProductSearchByWarehouseDto productDto);
}
