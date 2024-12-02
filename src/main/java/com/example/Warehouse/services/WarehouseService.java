package com.example.Warehouse.services;

import org.springframework.data.domain.Page;
import com.example.WarehouseContracts.dto.WarehouseDto;
import com.example.WarehouseContracts.dto.WarehouseAddDto;

public interface WarehouseService {
    WarehouseDto getById(String id);
    void delete(String warehouseId);
    String add(WarehouseAddDto warehouseDto);
    Page<WarehouseDto> getWarehouses(String substring, int page, int size);
}
