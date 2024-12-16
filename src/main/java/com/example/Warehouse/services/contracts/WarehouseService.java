package com.example.Warehouse.services.contracts;

import com.example.Warehouse.models.dto.PageForRedis;
import com.example.Warehouse.models.dto.order.OrderItemDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseAddDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseSearchDto;

import java.util.List;

public interface WarehouseService {

    void delete(String warehouseId);

    String add(WarehouseAddDto warehouseDto);

    void fill(String warehouseId, List<OrderItemDto> items);

    PageForRedis<WarehouseDto> findWarehouses(WarehouseSearchDto warehouseDto);
}
