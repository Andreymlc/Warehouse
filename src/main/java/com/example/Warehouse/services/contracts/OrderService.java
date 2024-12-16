package com.example.Warehouse.services.contracts;

import org.springframework.data.domain.Page;
import com.example.Warehouse.dto.order.OrderDto;

public interface OrderService {
    void addOrder(String username, String warehouseId);
    OrderDto findOrderByNumber(String number);
    Page<OrderDto> findOrders(String username, int page, int size);
}
