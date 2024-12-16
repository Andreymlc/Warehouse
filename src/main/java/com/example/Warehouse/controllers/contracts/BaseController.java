package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;

public interface BaseController {
    BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart);
}
