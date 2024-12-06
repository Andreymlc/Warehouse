package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.WarehouseContracts.dto.ProductDto;
import com.example.WarehouseContracts.dto.ProductAddDto;

import java.math.BigDecimal;

public interface ProductService {
    void deleteProduct(String productId);
    String addProduct(ProductAddDto productAddDto);
    void editProduct(String id, String name, String categoryName, BigDecimal price);

    ProductDto findById(String id);
    Page<ProductDto> findProducts(String substring, int page, int size, String category, boolean priceSort);
    Page<ProductDto> findProductsByWarehouse(String substring, int page, int size, String category, String warehouseId);
}
