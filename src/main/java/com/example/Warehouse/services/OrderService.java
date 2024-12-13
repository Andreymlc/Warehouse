package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.Warehouse.dto.OrderDto;

public interface OrderService {
    void addOrder(String userId, String warehouseId);
    OrderDto findOrderByNumber(String number);
    Page<OrderDto> findOrders(String userId, int page, int size);
}
