package com.example.Warehouse.domain.repository.impl;

import org.springframework.stereotype.Repository;
import com.example.Warehouse.domain.models.CartItem;
import com.example.Warehouse.domain.repository.contracts.cart.CartRepository;
import com.example.Warehouse.domain.repository.contracts.cart.BaseCartRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class CartRepositoryImpl extends BaseRepository<BaseCartRepository> implements CartRepository {

    @Override
    public CartItem save(CartItem cart) {
        return repository.save(cart);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll(List<CartItem> items) {
        repository.deleteAll(items);
    }

    @Override
    public int findProductQuantityByUserId(String userId) {
        return repository.countByUserId(userId);
    }

    @Override
    public Optional<List<CartItem>> findAllByUserId(String userid) {
        return repository.findAllByUserId(userid);
    }
}
