package com.example.Warehouse.controllers.impl;

import com.example.Warehouse.services.contracts.CategoryService;
import com.example.Warehouse.services.contracts.ProductService;
import com.example.Warehouse.services.contracts.WarehouseService;
import com.example.Warehouse.controllers.contracts.HomeController;
import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeControllerImpl implements HomeController {
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    public HomeControllerImpl(
        ModelMapper modelMapper,
        ProductService productService,
        CategoryService categoryService,
        WarehouseService warehouseService
    ) {
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
    }
    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
