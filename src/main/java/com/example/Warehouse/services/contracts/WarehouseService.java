package com.example.Warehouse.services.contracts;

import com.example.Warehouse.dto.PageForRedis;
import com.example.Warehouse.dto.order.OrderItemDto;
import com.example.Warehouse.dto.warehouse.WarehouseDto;
import com.example.Warehouse.dto.warehouse.WarehouseAddDto;
import com.example.Warehouse.dto.warehouse.WarehouseSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WarehouseService {

    void delete(String warehouseId);

    String add(WarehouseAddDto warehouseDto);

    void fill(String warehouseId, List<OrderItemDto> items);

    PageForRedis<WarehouseDto> findWarehouses(WarehouseSearchDto warehouseDto);
}
