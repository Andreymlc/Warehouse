package com.example.Warehouse.domain.repositories.contracts.cart;

import com.example.Warehouse.domain.models.CartItem;
import com.example.Warehouse.domain.repositories.contracts.BaseSaveRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends BaseSaveRepository<CartItem> {

    int findItemsQuantityByUserId(String userId);

    Optional<List<CartItem>> findAllByUserId(String userid);

}
