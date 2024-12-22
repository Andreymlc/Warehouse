package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.entities.Product;
import com.example.Warehouse.domain.entities.Stock;
import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import com.example.Warehouse.domain.repositories.contracts.product.ProductRepository;
import com.example.Warehouse.domain.repositories.contracts.stock.StockRepository;
import com.example.Warehouse.models.dto.product.*;
import com.example.Warehouse.models.filters.ProductFilter;
import com.example.Warehouse.models.filters.StockFilter;
import com.example.Warehouse.services.contracts.ProductService;
import com.example.Warehouse.services.contracts.StockService;
import com.example.Warehouse.utils.specifications.ProductSpec;
import com.example.Warehouse.utils.specifications.StockSpec;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final StockService stockService;
    private final StockRepository stockRepo;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    private static final Logger LOG = LogManager.getLogger(ProductServiceImpl.class);

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
    @Cacheable(value = "products")
    public Page<ProductDto> findProducts(ProductSearchDto productDto) {
        LOG.info("Cache not found. findProducts called, params: {}", productDto);

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

        return productsPage.map(p ->
            new ProductDto(
                p.getId(),
                p.getName(),
                (float) Math.round(p.getPrice() * p.getCategory().getDiscount() * 100) / 100,
                p.getPrice(),
                p.getCategory().getName(),
                stockService.findProductQuantityByProductId(p.getId()),
                p.getIsDeleted()
            )
        );
    }

    @Override
    @Cacheable(value = "stocks")
    public Page<ProductStockDto> findProductsByWarehouse(ProductSearchByWarehouseDto productDto) {
        LOG.info("Cache not found. findProductsByWarehouse called, params: {}", productDto);

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

        return stoksPage.map(s -> {
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
            }
        );
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public String addProduct(ProductAddDto productAddDto) {
        LOG.info("Cache 'products' is cleared. addProduct called, params: {}", productAddDto);

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
    @Caching(evict = {
        @CacheEvict(value = "product", key = "#productId"),
        @CacheEvict(value = {"products", "stocks", "cart"}, allEntries = true)
    })
    public void deleteProduct(String productId) {
        LOG.info("Cache 'products, stocks, cart, productId' is cleared. deleteProduct called, productId - {}", productId);

        productRepo.findById(productId)
            .ifPresent(p -> {
                p.setIsDeleted(true);
                productRepo.save(p);
            });
    }

    @Override
    @CacheEvict(value = {"products", "stocks", "cart"}, allEntries = true)
    public void editProduct(ProductEditDto productDto) {
        LOG.info("Cache 'products, stocks, cart' is cleared. editProduct called, params: {}", productDto);

        var product = productRepo.findById(productDto.id())
            .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        product.setName(productDto.name());

        var category = categoryRepo.findByName(productDto.categoryName())
            .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        product.setCategory(category);
        product.setPrice(productDto.price());

        productRepo.save(product);
    }

    @Override
    @Cacheable("most-popular")
    public List<ProductDto> findFiveMostPopular() {
        return productRepo.findFiveMostPopular()
            .stream()
            .map(p -> new ProductDto(
                p.getId(),
                p.getName(),
                (float) Math.round(p.getPrice() * p.getCategory().getDiscount() * 100) / 100,
                p.getPrice(),
                p.getCategory().getName(),
                stockService.findProductQuantityByProductId(p.getId()),
                p.getIsDeleted()
            ))
            .toList();
    }
}
