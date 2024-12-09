package com.example.Warehouse.dto;

import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductViewModel;

import java.util.List;

public record CartDto(
    Float totalPrice,
    List<ProductCartDto> products
) {}
