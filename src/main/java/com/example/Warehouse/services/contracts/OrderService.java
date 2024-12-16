package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.PageForRedis;
import org.springframework.data.domain.Page;
import com.example.Warehouse.models.dto.order.OrderDto;

public interface OrderService {
    void addOrder(String username, String warehouseId);
    PageForRedis<OrderDto> findOrders(String username, int page, int size);
}
