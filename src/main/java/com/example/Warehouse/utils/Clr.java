package com.example.Warehouse.utils;

import com.example.Warehouse.dto.AddStockDto;
import com.example.Warehouse.dto.CategoryAddDto;
import com.example.Warehouse.dto.ProductAddDto;
import com.example.Warehouse.dto.WarehouseAddDto;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import com.example.Warehouse.services.StockService;
import com.example.Warehouse.services.ProductService;
import com.example.Warehouse.services.CategoryService;
import com.example.Warehouse.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.math.BigDecimal;

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
        WarehouseService warehouseService
    ) {
        this.stockService = stockService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
    }

    @Override
    @Transactional
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
                warehousesIds.add(warehouseService.add(warehouse));
            }

            var initialCategories = Arrays.asList(
                    new CategoryAddDto("Мебель", 1),
                    new CategoryAddDto("Одежда", 1),
                    new CategoryAddDto("Концелярия", 1),
                    new CategoryAddDto("Обувь", 1),
                    new CategoryAddDto("Еда", 1)
            );

            for (var category: initialCategories) {
                System.out.println("Id: " + categoryService.addCategory(category));
            }

            List<ProductAddDto> initialProducts = Arrays.asList(
                    new ProductAddDto("Помидор", "Еда", 10f),
                    new ProductAddDto("Огурец", "Еда", 11f),
                    new ProductAddDto("Стол", "Мебель", 1999.99f),
                    new ProductAddDto("Стул", "Мебель", 989.99f),
                    new ProductAddDto("Футболка", "Одежда", 4115.99f),
                    new ProductAddDto("Штаны", "Одежда", 5199.99f),
                    new ProductAddDto("Куртка", "Одежда", 8129.99f),
                    new ProductAddDto("Ботинки", "Одежда", 15199.99f),
                    new ProductAddDto("Торт", "Еда", 599.99f),
                    new ProductAddDto("Сметана", "Еда", 87.99f),
                    new ProductAddDto("Шкаф", "Мебель", 10111.99f),
                    new ProductAddDto("Полка", "Мебель", 1199.99f)
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
                            1,
                            20
                        )
                    );
                }
            }

            System.out.println("Начальные данные добавлены успешно");
        }
    }
}
