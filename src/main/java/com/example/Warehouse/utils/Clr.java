package com.example.Warehouse.utils;

import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.entities.Role;
import com.example.Warehouse.domain.repositories.contracts.user.roles.RoleRepository;
import com.example.Warehouse.models.dto.category.CategoryAddDto;
import com.example.Warehouse.models.dto.product.ProductAddDto;
import com.example.Warehouse.models.dto.warehouse.AddStockDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseAddDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseSearchDto;
import com.example.Warehouse.services.contracts.CategoryService;
import com.example.Warehouse.services.contracts.ProductService;
import com.example.Warehouse.services.contracts.StockService;
import com.example.Warehouse.services.contracts.WarehouseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class Clr implements CommandLineRunner {
    private final StockService stockService;
    private final ProductService productService;
    private final RoleRepository roleRepository;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;

    private static final Logger LOG = LogManager.getLogger(Clr.class);

    @Autowired
    public Clr(
        StockService stockService,
        ProductService productService, RoleRepository roleRepository,
        CategoryService categoryService,
        WarehouseService warehouseService
    ) {
        this.stockService = stockService;
        this.productService = productService;
        this.roleRepository = roleRepository;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Random random = new Random();
        var existingWarehouse = warehouseService.findWarehouses(new WarehouseSearchDto(1, 12, "", true));

        if (existingWarehouse.isEmpty()) {
            LOG.info("База дынных пуста. Заполняем..");

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
            for (var warehouse : initialWarehouses) {
                warehousesIds.add(warehouseService.add(warehouse));
            }

            var initialCategories = Arrays.asList(
                new CategoryAddDto("Мебель", 1),
                new CategoryAddDto("Одежда", 1),
                new CategoryAddDto("Концелярия", 1),
                new CategoryAddDto("Обувь", 1),
                new CategoryAddDto("Еда", 1)
            );

            for (var category : initialCategories) {
                categoryService.create(category);
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
            for (var product : initialProducts) {
                productsIds.add(productService.addProduct(product));
            }

            for (var warehouse : warehousesIds) {
                for (var product : productsIds) {
                    stockService.addStock(
                        new AddStockDto(
                            random.nextInt(9) + 1,
                            product,
                            warehouse,
                            1,
                            20
                        )
                    );
                }
            }

            roleRepository.save(new Role(Roles.USER));
            roleRepository.save(new Role(Roles.ADMIN));

            LOG.info("Начальные данные добавлены успешно");
        }
    }
}
