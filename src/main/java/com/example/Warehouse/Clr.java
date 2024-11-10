package com.example.Warehouse;

import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.models.Category;
import com.example.Warehouse.domain.models.Product;
import com.example.Warehouse.domain.models.User;
import com.example.Warehouse.domain.models.Warehouse;
import com.example.Warehouse.domain.repository.CategoryRepository;
import com.example.Warehouse.domain.repository.ProductRepository;
import com.example.Warehouse.domain.repository.UserRepository;
import com.example.Warehouse.domain.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Component
public class Clr implements CommandLineRunner {
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;

    @Autowired
    public Clr(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository,
            UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hi");

        while (true) {
            System.out.println("""
                    Choose option from:
                    1 - for Add Category
                    2 - for Add Product
                    3 - for Add Order
                    4 - for Add Purchase
                    5 - for Add User
                    6 - for Warehouse
                    7 - for Update discount for category
                    """);

            String input = bufferedReader.readLine().toLowerCase();

            switch (input) {
                case "1" -> this.addCategory();
                case "2" -> this.addProduct();
                case "3" -> this.addProduct();
                case "4" -> this.addProduct();
                case "5" -> this.addUser();
                case "6" -> this.addWarehouse();
                case "7" -> this.updateDiscountForCategory();
                case "8" -> this.addWarehouse();
                case "9" -> this.addWarehouse();
                default -> System.err.println("Invalid input\n");
            }
            System.out.println("==================================");
        }
    }

    private void addProduct() throws IOException {
        System.out.println("Enter product details in format: name price category");
        String[] productParams = this.bufferedReader.readLine()
                .split("\\s+");

        Category category = categoryRepository.findByName(productParams[2]).orElseThrow();
        Product product = new Product(productParams[0], new BigDecimal(productParams[1]), category);

        try {
            this.productRepository.save(product);
            System.out.println("Successfully added product!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add product!");
        }
    }

    private void addCategory() throws IOException {
        System.out.println("Enter category details in format: name discount");

        var categoryParams = getParams();
        Category category = new Category(categoryParams[0], Float.parseFloat(categoryParams[1]));

        try {
            this.categoryRepository.save(category);
            System.out.println("Successfully added category!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add category!");
        }
    }

    private void addWarehouse() throws IOException {
        System.out.println("Enter warehouse details in format: name location");
        var warehouseParams = getParams();
        Warehouse warehouse = new Warehouse(warehouseParams[0], warehouseParams[1]);

        try {
            this.warehouseRepository.save(warehouse);
            System.out.println("Successfully added warehouse!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add warehouse!");
        }
    }

    private void addUser() throws IOException {
        System.out.println("Enter user details in format: name email passwordHash role(admin, consumer) points");
        var userParams = getParams();

        var role = userParams[3].equals("admin") ? Roles.ADMIN : Roles.CONSUMER;

        User user = new User(userParams[0], userParams[1], userParams[2], role, Integer.parseInt(userParams[4]));

        try {
            this.userRepository.save(user);
            System.out.println("Successfully added warehouse!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add warehouse!");
        }
    }

    private void updateDiscountForCategory() throws IOException, ClassNotFoundException {
        System.out.println("Enter new discount categories in format: nameCategory newDiscount");

        var newDiscountParams = getParams();

        Category category = categoryRepository.findByName(newDiscountParams[0]).orElseThrow();

        try {
            this.categoryRepository.updateDiscount(newDiscountParams[0], Float.parseFloat(newDiscountParams[1]));
            System.out.println("Successfully added category!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add category!" + e.getMessage());
        }
    }

    private String[] getParams() throws IOException {
        return this.bufferedReader.readLine().split("\\s+");
    }
}
