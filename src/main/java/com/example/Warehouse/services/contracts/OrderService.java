package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.order.OrderDto;
import org.springframework.data.domain.Page;

public interface OrderService {
    void addOrder(String username, String warehouseId);
    Page<OrderDto> findOrders(String username, int page, int size);
}
