package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.WarehouseContracts.dto.ProductDto;
import com.example.WarehouseContracts.dto.ProductAddDto;

public interface ProductService {
    ProductDto getById(String id);
    void deleteProduct(String productId);
    String addProduct(ProductAddDto productAddDto);
    Page<ProductDto> getProducts(String substring, int page, int size, String category, boolean priceSort);
    Page<ProductDto> getProductsByWarehouse(String substring, int page, int size, String category, String warehouseId);
}
