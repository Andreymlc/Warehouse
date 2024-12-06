package com.example.Warehouse.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;
import com.example.Warehouse.domain.models.Product;
import org.springframework.data.domain.PageRequest;
import com.example.Warehouse.services.StockService;
import com.example.Warehouse.services.ProductService;
import com.example.WarehouseContracts.dto.ProductDto;
import com.example.WarehouseContracts.dto.ProductAddDto;
import com.example.Warehouse.domain.repository.CategoryRepository;
import com.example.Warehouse.domain.repository.contracts.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ProductServiceImpl implements ProductService {
    private final StockService stockService;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductServiceImpl(
        StockService stockService,
        ProductRepository productRepo,
        CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.stockService = stockService;
    }

    @Override
    public Page<ProductDto> findProducts(
        String substring,
        int page,
        int size,
        String category,
        boolean priceSort
    ) {
        Sort sort = priceSort
            ? Sort.by("price").ascending()
            : Sort.by("price").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Product> productPage;

        if (!substring.isBlank() && !category.isBlank()) {
            productPage = productRepo
                .findByCategoryNameAndNameContainingIgnoreCase(category, substring, pageable);
        }
        else if (!substring.isBlank()) {
            productPage = productRepo
                .findByNameContainingIgnoreCase(substring, pageable);
        }
        else if (!category.isBlank()) {
            productPage = productRepo.findByCategoryName(category, pageable);
        }
        else {
            productPage = productRepo.findAll(pageable);
        }

        return productPage.map(p -> new ProductDto(
                p.getId(),
                p.getName(),
                p.getCategory().getName(),
                p.getPrice()
                    .multiply(BigDecimal.valueOf(p.getCategory().getDiscount()))
                    .setScale(2, RoundingMode. HALF_UP),
                stockService.getProductQuantityByProductId(p.getId()),
                p.getPrice()
            )
        );
    }

    @Override
    public Page<ProductDto> findProductsByWarehouse(
        String substring,
        int page,
        int size,
        String category,
        String warehouseId
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Product> products;

        if (!substring.isBlank() && !category.isBlank()) {
            products = productRepo
                .findByStocksWarehouseIdAndNameContainingIgnoreCase(warehouseId, substring, pageable);
        }
        else if (!substring.isBlank()) {
            products = productRepo
                .findByStocksWarehouseIdAndNameContainingIgnoreCase(warehouseId, substring, pageable);
        }
        else if (!category.isBlank()) {
            products = productRepo
                .findByStocksWarehouseIdAndCategoryName(warehouseId, category, pageable);
        }
        else {
            products = productRepo.findByStocksWarehouseId(warehouseId, pageable);
        }

        return products.map(p -> new ProductDto(
                p.getId(),
                p.getName(),
                p.getCategory().getName(),
                p.getPrice()
                .multiply(BigDecimal.valueOf(p.getCategory().getDiscount()))
                .setScale(2, RoundingMode.HALF_UP),
                stockService.getProductQuantityByWarehouseId(p.getId(), warehouseId),
                p.getPrice()
            )
        );
    }

    @Override
    public String addProduct(ProductAddDto productAddDto) {
        var existingCategory = categoryRepo.findByName(productAddDto.category())
            .orElseThrow(() ->
                new EntityNotFoundException("Категория не найдена"));

        return productRepo.saveProduct(
            new Product(
                productAddDto.name(),
                productAddDto.price(),
                existingCategory
            )
        ).getId();
    }

    @Override
    public void deleteProduct(String productId) {
        productRepo.deleteById(productId);
    }

    @Override
    public ProductDto findById(String id) {
        var product =  productRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));
        var category = product.getCategory();

        return new ProductDto(
            product.getId(),
            product.getName(),
            category.getName(),
            product
                .getPrice()
                .multiply(BigDecimal.valueOf(category.getDiscount()))
                .setScale(2, RoundingMode. HALF_UP),
            stockService.getProductQuantityByProductId(product.getId()),
            product.getPrice()
        );
    }

    public void editProduct(String id, String name, String categoryName, BigDecimal price) {
        var product = productRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        var isChanged = false;

        if (!product.getName().equalsIgnoreCase(name)) {
            product.setName(name);
            isChanged = true;
        }
        if (product.getCategory().getName().equalsIgnoreCase(categoryName)) {
            var category = categoryRepo.findByName(categoryName)
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

            product.setCategory(category);
            isChanged = true;
        }
        if (!product.getPrice().equals(price)) {
            product.setPrice(price);
            isChanged = true;
        }
        if (isChanged) {
            productRepo.saveProduct(product);
        }
    }
}
