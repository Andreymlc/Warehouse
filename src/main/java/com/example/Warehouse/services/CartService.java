package com.example.Warehouse.services;

import com.example.Warehouse.dto.CartDto;

public interface CartService {
    CartDto findCart(String userId);
    int getItemQuantity(String userId);
    void deleteProductFromCart(String userId, String productId);
    void addProductToCart(String userId, String productId);
}
