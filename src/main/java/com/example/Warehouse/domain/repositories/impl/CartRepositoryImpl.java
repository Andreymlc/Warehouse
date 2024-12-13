package com.example.Warehouse.domain.repositories.impl;

import com.example.Warehouse.domain.models.CartItem;
import com.example.Warehouse.domain.repositories.contracts.cart.BaseCartRepository;
import com.example.Warehouse.domain.repositories.contracts.cart.CartRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartRepositoryImpl extends BaseRepository<BaseCartRepository> implements CartRepository {

    @Override
    public CartItem save(CartItem cart) {
        return repository.save(cart);
    }

    @Override
    public int findItemsQuantityByUserId(String userId) {
        return repository.countByUserId(userId);
    }

    @Override
    public Optional<List<CartItem>> findAllByUserId(String userid) {
        return repository.findAllByUserId(userid);
    }
}
