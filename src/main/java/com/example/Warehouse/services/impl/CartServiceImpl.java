package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.models.CartItem;
import com.example.Warehouse.domain.repositories.contracts.cart.CartRepository;
import com.example.Warehouse.domain.repositories.contracts.product.ProductRepository;
import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import com.example.Warehouse.dto.CartDto;
import com.example.Warehouse.dto.ProductCartDto;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.services.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartServiceImpl(
        UserRepository userRepo,
        CartRepository cartRepo,
        ProductRepository productRepo
    ) {
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    @Override
    public int getItemsQuantity(String userId) {
        return cartRepo.findItemsQuantityByUserId(userId);
    }

    @Override
    public CartDto findCart(String userId) {
        var cart = cartRepo.findAllByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("Корзина не найдена"));

        List<ProductCartDto> productCartDto = new ArrayList<>();

        float totalPrice = 0;

        for (var item : cart) {
            var product = item.getProduct();
            var category = product.getCategory();

            productCartDto.add(new ProductCartDto(
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
    public void addProductToCart(String userId, String productId) {
        var existingUser = userRepo.findById(userId)
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
    public void deleteProductFromCart(String userId, String productId) {
        var existingUser = userRepo.findById(userId)
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
