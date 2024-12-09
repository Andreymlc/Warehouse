package com.example.Warehouse.domain.repository.contracts.cart;

import com.example.Warehouse.domain.models.CartItem;
import com.example.Warehouse.domain.repository.contracts.BaseSaveRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends BaseSaveRepository<CartItem> {
    void deleteById(String id);
    void deleteAll(List<CartItem> items);
    int findProductQuantityByUserId(String userId);
    Optional<List<CartItem>> findAllByUserId(String userid);

}
