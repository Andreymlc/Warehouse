package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.models.Product;
import com.example.Warehouse.domain.models.Stock;
import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import com.example.Warehouse.domain.repositories.contracts.product.ProductRepository;
import com.example.Warehouse.domain.repositories.contracts.stock.StockRepository;
import com.example.Warehouse.dto.PageForRedis;
import com.example.Warehouse.dto.product.*;
import com.example.Warehouse.dto.filters.ProductFilter;
import com.example.Warehouse.dto.filters.StockFilter;
import com.example.Warehouse.services.contracts.ProductService;
import com.example.Warehouse.services.contracts.StockService;
import com.example.Warehouse.utils.specifications.ProductSpec;
import com.example.Warehouse.utils.specifications.StockSpec;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final StockService stockService;
    private final StockRepository stockRepo;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductServiceImpl(
        StockService stockService,
        StockRepository stockRepo,
        ProductRepository productRepo,
        CategoryRepository categoryRepo
    ) {
        this.stockRepo = stockRepo;
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.stockService = stockService;
    }

    @Override
    @Cacheable(
        value = "products",
        key = "#productDto.page + '-' + " +
            "#productDto.size + '-' + " +
            "#productDto.category + '-' + " +
            "#productDto.substring + '-' + " +
            "#productDto.priceSort + '-' +" +
            "#productDto.deleted"
    )
    public PageForRedis<ProductDto> findProducts(ProductSearchDto productDto) {
        var sort = productDto.priceSort().equals("asc")
            ? Sort.by("price").ascending()
            : Sort.by("price").descending();

        var pageable = PageRequest.of(productDto.page() - 1, productDto.size(), sort);

        Page<Product> productsPage = productRepo.findAllByFilter(
            ProductSpec.filter(
                new ProductFilter(
                    productDto.category(),
                    productDto.isDeleted(),
                    productDto.substring()
                )
            ),
            pageable
        );

        return new PageForRedis<>(productsPage.map(p ->
            new ProductDto(
                p.getId(),
                p.getName(),
                (float) Math.round(p.getPrice() * p.getCategory().getDiscount() * 100) / 100,
                p.getPrice(),
                p.getCategory().getName(),
                stockService.findProductQuantityByProductId(p.getId()),
                p.getIsDeleted()
            )
        ));
    }

    @Override
    @Cacheable(
        value = "stocks",
        key = "#productDto.page + '-' + " +
            "#productDto.size + '-' + " +
            "#productDto.category + '-' + " +
            "#productDto.substring + '-' + " +
            "#productDto.warehouseId + '-' +" +
            "#productDto.returnDeletedProduct"
    )
    public PageForRedis<ProductStockDto> findProductsByWarehouse(ProductSearchByWarehouseDto productDto) {
        var pageable = PageRequest.of(productDto.page() - 1, productDto.size(), Sort.by("quantity"));

        Page<Stock> stoksPage = stockRepo.findAllByFilter(
            StockSpec.filter(
                new StockFilter(
                    productDto.category(),
                    productDto.substring(),
                    productDto.warehouseId(),
                    productDto.returnDeletedProduct()
                )
            ),
            pageable
        );

        return new PageForRedis<>(
            stoksPage.map(s -> {
                var product = s.getProduct();

                return new ProductStockDto(
                    product.getId(),
                    product.getName(),
                    s.getQuantity(),
                    s.getMinStock(),
                    s.getMaxStock(),
                    product.getCategory().getName(),
                    product.getIsDeleted()
                );
            })
        );
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public String addProduct(ProductAddDto productAddDto) {
        var existingCategory = categoryRepo.findByName(productAddDto.category())
            .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        return productRepo.save(
            new Product(
                productAddDto.name(),
                productAddDto.price(),
                existingCategory,
                false
            )
        ).getId();
    }

    @Override
    @CacheEvict(value = {"products", "stocks"}, allEntries = true)
    public void deleteProduct(String productId) {
        var product = productRepo.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        product.setIsDeleted(true);
        productRepo.save(product);
    }

    @Override
    @CacheEvict(value = {"products", "stocks"}, allEntries = true)
    public void editProduct(String id, String name, String categoryName, float price) {
        var product = productRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        product.setName(name);

        var category = categoryRepo.findByName(categoryName)
            .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        product.setCategory(category);
        product.setPrice(price);

        productRepo.save(product);
    }
}
