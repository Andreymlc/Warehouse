package com.example.Warehouse.services.impl;

import com.example.Warehouse.services.StockService;
import com.example.WarehouseContracts.dto.ProductAddDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Product;
import org.springframework.data.domain.PageRequest;
import com.example.Warehouse.services.ProductService;
import com.example.WarehouseContracts.dto.ProductDto;
import com.example.Warehouse.domain.repository.ProductRepository;
import com.example.Warehouse.domain.repository.CategoryRepository;

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
    public Page<ProductDto> getProducts(String substring, int page, int size, String category, boolean priceSort) {
        Sort sort = priceSort ? Sort.by("price").ascending() : Sort.by("price").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Product> productPage;

        if (!substring.isBlank() && !category.isBlank()) {
            productPage = productRepo.findByCategoryNameAndNameContainingIgnoreCase(category, substring, pageable);
        }
        else if (!substring.isBlank()) {
            productPage = productRepo.findByNameContainingIgnoreCase(substring, pageable);
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
                stockService.getQuantityByProductId(p.getId()),
                p.getPrice()
            )
        );
    }

    @Override
    public String addProduct(ProductAddDto productAddDto) {
        var existingCategory = categoryRepo.findByName(productAddDto.category());
        return productRepo.save(
            new Product(
                productAddDto.name(),
                productAddDto.price(),
                existingCategory.orElseThrow(),
                false
            )
        ).getId();
    }

    @Override
    public ProductDto getById(String id) {
        var product =  productRepo.findById(id).orElseThrow();
        var category = product.getCategory();

        return new ProductDto(
            product.getId(),
            product.getName(),
            category.getName(),
            product
                .getPrice()
                .multiply(BigDecimal.valueOf(category.getDiscount()))
                .setScale(2, RoundingMode. HALF_UP),
            stockService.getQuantityByProductId(product.getId()),
            product.getPrice()
        );
    }
}
