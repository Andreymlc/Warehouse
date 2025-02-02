package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.entities.CartItem;
import com.example.Warehouse.domain.repositories.contracts.product.ProductRepository;
import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.models.dto.cart.CartDto;
import com.example.Warehouse.models.dto.product.ProductCartDto;
import com.example.Warehouse.services.contracts.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    private static final Logger LOG = LogManager.getLogger(Service.class);

    public CartServiceImpl(
            UserRepository userRepo,
            ProductRepository productRepo
    ) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Cacheable("cart-items-count")
    public int findCountItemsInCart(String username) {
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        return user.getCart().size();
    }

    @Override
    @Cacheable("cart")
    public CartDto findCart(String username) {
        LOG.info("Cache not found. findCart called, username - {}", username);

        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        var cart = user.getCart();

        List<ProductCartDto> productCartDto = new ArrayList<>();

        float totalPrice = 0;

        for (var item : cart) {
            var product = item.getProduct();
            var category = product.getCategory();

            productCartDto.add(
                new ProductCartDto(
                    product.getId(),
                    product.getName(),
                    category.getName(),
                    item.getQuantity(),
                    (float) Math.round(product.getPrice() * category.getDiscount() * 100) / 100 * item.getQuantity()
                )
            );

            totalPrice += productCartDto.getLast().totalPrice();
        }

        return new CartDto(totalPrice, productCartDto);
    }

    @Override
    @CacheEvict(value = "cart", key = "#username")
    public void addProductToCart(String username, String productId) {
        LOG.info(
                "Cache 'cart' is cleared. addProductToCart called, params: username: {}, productId: {}",
                username, productId
        );

        var existingUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        var existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        var existingCart = existingUser.getCart();

        var existingCartItem = existingCart.stream()
                .filter(c -> c.getProduct().getId().equals(existingProduct.getId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            var item = existingCartItem.get();
            item.incrQuantity();
            userRepo.save(existingUser);
        } else {
            CartItem newCartItem = new CartItem(existingUser, existingProduct, 1);
            existingCart.add(newCartItem);
            userRepo.save(existingUser);
        }
    }

    @Override
    @CacheEvict(value = "cart", key = "#username")
    public void deleteProductFromCart(String username, String productId) {
        LOG.info(
                "Cache 'cart' is cleared. deleteProductFromCart called, params: username: {}, productId: {}",
                username, productId
        );

        var existingUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        var existingCart = existingUser.getCart();

        var existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        var existingCartItem = existingCart.stream()
                .filter(c -> c.getProduct().getId().equals(existingProduct.getId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            var item = existingCartItem.get();

            if (item.getQuantity() > 1) {
                item.decrQuantity();
                userRepo.save(existingUser);
            } else if (item.getQuantity() == 1) {
                existingCart.remove(item);
                userRepo.save(existingUser);
            }
        } else {
            throw new InvalidDataException("Продукт для удаления не найден");
        }
    }
}
