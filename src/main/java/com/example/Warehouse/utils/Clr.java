package com.example.Warehouse.utils;

import com.example.WarehouseContracts.dto.*;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import com.example.Warehouse.services.StockService;
import com.example.Warehouse.services.ProductService;
import com.example.Warehouse.services.CategoryService;
import com.example.Warehouse.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.math.BigDecimal;
import java.util.Random;

@Component
public class Clr implements CommandLineRunner {
    private final StockService stockService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;

    @Autowired
    public Clr(
        StockService stockService,
        ProductService productService,
        CategoryService categoryService,
        WarehouseService warehouseService) {
        this.stockService = stockService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
    }

    @Override
    public void run(String... args) {
        Random random = new Random();
        var existingWarehouse = warehouseService.getWarehouses("", 1, 5);

        if (existingWarehouse.isEmpty()) {
            System.out.println("База дынных пуста. Заполняем..");

            List<WarehouseAddDto> initialWarehouses = Arrays.asList(
                    new WarehouseAddDto("Склад 1", "Останкино"),
                    new WarehouseAddDto("Склад 2", "Чертаново"),
                    new WarehouseAddDto("Склад 3", "ВДНХ"),
                    new WarehouseAddDto("Склад 4", "Бутово"),
                    new WarehouseAddDto("Склад 5", "Электросталь"),
                    new WarehouseAddDto("Склад 6", "Новокузнецк"),
                    new WarehouseAddDto("Склад 7", "Санкт-Петербург"),
                    new WarehouseAddDto("Склад 8", "Краснодар"),
                    new WarehouseAddDto("Склад 9", "Казань")
            );

            List<String> warehousesIds = new ArrayList<>();
            for (var warehouse: initialWarehouses) {
                warehousesIds.add(warehouseService.addWarehouse(warehouse));
            }

            var initialCategories = Arrays.asList(
                    new CategoryDto("Мебель", 0f),
                    new CategoryDto("Одежда", 0f),
                    new CategoryDto("Еда", 0f)
            );

            for (var category: initialCategories) {
                System.out.println("Id: " + categoryService.addCategory(category));
            }

            List<ProductAddDto> initialProducts = Arrays.asList(
                    new ProductAddDto("Помидор", "Еда", BigDecimal.valueOf(10), BigDecimal.valueOf(10)),
                    new ProductAddDto("Огурец", "Еда", BigDecimal.valueOf(11),BigDecimal.valueOf(11)),
                    new ProductAddDto("Стол", "Мебель", BigDecimal.valueOf(1999.99), BigDecimal.valueOf(1999.99)),
                    new ProductAddDto("Стул", "Мебель", BigDecimal.valueOf(989.99), BigDecimal.valueOf(989.99)),
                    new ProductAddDto("Футболка", "Одежда", BigDecimal.valueOf(4115.99), BigDecimal.valueOf(4115.99)),
                    new ProductAddDto("Штаны", "Одежда", BigDecimal.valueOf(5199.99), BigDecimal.valueOf(5199.99)),
                    new ProductAddDto("Куртка", "Одежда", BigDecimal.valueOf(8129.99), BigDecimal.valueOf(8129.99)),
                    new ProductAddDto("Ботинки", "Одежда", BigDecimal.valueOf(15199.99), BigDecimal.valueOf(15199.99)),
                    new ProductAddDto("Торт", "Еда", BigDecimal.valueOf(599.99), BigDecimal.valueOf(599.99)),
                    new ProductAddDto("Сметана", "Еда", BigDecimal.valueOf(87.99), BigDecimal.valueOf(87.99)),
                    new ProductAddDto("Шкаф", "Мебель", BigDecimal.valueOf(10111.99), BigDecimal.valueOf(10111.99)),
                    new ProductAddDto("Полка", "Мебель", BigDecimal.valueOf(1199.99), BigDecimal.valueOf(1199.99))
            );

            List<String> productsIds = new ArrayList<>();
            for (var product: initialProducts) {
                productsIds.add(productService.addProduct(product));
            }

            for (var warehouse : warehousesIds) {
                for (var product : productsIds) {
                    stockService.addStock(
                        new AddStockDto(
                            random.nextInt(10),
                            product,
                            warehouse,
                            1
                        )
                    );
                }
            }

            System.out.println("Начальные данные добавлены успешно");
        }
    }
}
