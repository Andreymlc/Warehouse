package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.WarehouseContracts.dto.OrderDto;

import java.math.BigDecimal;

public interface OrderService {
    OrderDto getOrderByNumber(String number);
    Page<OrderDto> getOrders(String status, boolean dateSort, int page, int size);
    String addOrder(String userId, String status, BigDecimal price);
}
