package com.example.Warehouse.domain.repositories.contracts.cart;

import com.example.Warehouse.domain.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BaseCartRepository extends JpaRepository<CartItem, String> {
    int countByUserId(String userId);

    Optional<List<CartItem>> findAllByUserId(String userid);
}
