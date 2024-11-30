package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.WarehouseContracts.dto.ProductDto;
import com.example.WarehouseContracts.dto.ProductAddDto;

public interface ProductService {
    String addProduct(ProductAddDto productAddDto);
    ProductDto getById(String id);
    Page<ProductDto> getProducts(String substring, int page, int size, String category, boolean priceSort);
}
