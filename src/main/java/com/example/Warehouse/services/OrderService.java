package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.Warehouse.dto.OrderDto;

import java.math.BigDecimal;

public interface OrderService {
    public void addOrder(String userId);
    OrderDto getOrderByNumber(String number);
    Page<OrderDto> getOrders(String status, boolean dateSort, int page, int size);
}
