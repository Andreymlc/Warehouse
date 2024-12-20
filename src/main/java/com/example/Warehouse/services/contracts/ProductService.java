package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.product.*;
import org.springframework.data.domain.Page;

public interface ProductService {
    void deleteProduct(String productId);

    String addProduct(ProductAddDto productAddDto);

    void editProduct(String id, String name, String categoryName, float price);

    Page<ProductDto> findProducts(ProductSearchDto productSearchDto);

    Page<ProductStockDto> findProductsByWarehouse(ProductSearchByWarehouseDto productDto);
}
