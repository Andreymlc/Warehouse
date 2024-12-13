package com.example.Warehouse.services;

import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.dto.ProductAddDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    void deleteProduct(String productId);

    String addProduct(ProductAddDto productAddDto);

    void editProduct(String id, String name, String categoryName, float price);

    Page<ProductDto> findProducts(int page, int size, String category, String substring, boolean priceSort);

    Page<ProductDto> findProductsByWarehouse(int page, int size, String category, String substring, String warehouseId);
}
