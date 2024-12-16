package com.example.Warehouse.models.viewmodels.warehouse;

import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;

import java.util.List;

public record WarehousesViewModel(
    BasePagesViewModel base,
    List<WarehouseViewModel> warehouses
) {
}
