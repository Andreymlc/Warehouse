package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.product.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    void deleteProduct(String productId);

    List<ProductDto> findFiveMostPopular();

    void editProduct(ProductEditDto productDto);

    String addProduct(ProductAddDto productAddDto);

    Page<ProductDto> findProducts(ProductSearchDto productSearchDto);

    Page<ProductStockDto> findProductsByWarehouse(ProductSearchByWarehouseDto productDto);
}
