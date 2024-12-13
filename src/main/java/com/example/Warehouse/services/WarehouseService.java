package com.example.Warehouse.services;

import com.example.Warehouse.dto.OrderItemDto;
import com.example.Warehouse.dto.WarehouseDto;
import com.example.Warehouse.dto.WarehouseAddDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WarehouseService {

    void delete(String warehouseId);

    String add(WarehouseAddDto warehouseDto);

    void fill(String warehouseId, List<OrderItemDto> items);

    Page<WarehouseDto> findWarehouses(String substring, int page, int size);
}
