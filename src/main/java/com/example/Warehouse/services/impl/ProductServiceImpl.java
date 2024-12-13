package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.models.Product;
import com.example.Warehouse.domain.models.Stock;
import com.example.Warehouse.domain.repositories.contracts.category.CategoryRepository;
import com.example.Warehouse.domain.repositories.contracts.product.ProductRepository;
import com.example.Warehouse.domain.repositories.contracts.stock.StockRepository;
import com.example.Warehouse.dto.ProductAddDto;
import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.dto.filters.ProductFilter;
import com.example.Warehouse.dto.filters.StockFilter;
import com.example.Warehouse.services.ProductService;
import com.example.Warehouse.services.StockService;
import com.example.Warehouse.utils.specifications.ProductSpec;
import com.example.Warehouse.utils.specifications.StockSpec;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<ProductDto> findProducts(
        int page,
        int size,
        String category,
        String substring,
        boolean priceSort
    ) {
        Sort sort = priceSort
            ? Sort.by("price").ascending()
            : Sort.by("price").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Product> productsPage = productRepo.findAllByFilter(
            ProductSpec.filter(new ProductFilter(category, substring)),
            pageable
        );

        return productsPage.map(p ->
            new ProductDto(
                p.getId(),
                p.getName(),
                p.getCategory().getName(),
                (float) Math.round(p.getPrice() * p.getCategory().getDiscount() * 100) / 100,
                stockService.findProductQuantityByProductId(p.getId()),
                p.getPrice()
            )
        );
    }

    @Override
    public Page<ProductDto> findProductsByWarehouse(
        int page,
        int size,
        String category,
        String substring,
        String warehouseId
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("quantity"));

        Page<Stock> productsPage = stockRepo.findAllByFilter(
            StockSpec.filter(new StockFilter(category, substring, warehouseId)),
            pageable
        );

        return productsPage.map(s -> {
            var product = s.getProduct();

            return new ProductDto(
                product.getId(),
                product.getName(),
                product.getCategory().getName(),
                (float) Math.round(product.getPrice() * product.getCategory().getDiscount() * 100) / 100,
                s.getQuantity(),
                product.getPrice()
            );
        });
    }

    @Override
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
    public void deleteProduct(String productId) {
        var product = productRepo.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        product.setDeleted(true);
        productRepo.save(product);
    }

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
