package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.cart.CartDto;

public interface CartService {
    CartDto findCart(String username);

    void deleteProductFromCart(String username, String productId);

    void addProductToCart(String username, String productId);
}
