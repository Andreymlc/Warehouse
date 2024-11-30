package com.example.Warehouse.controllers;

import com.example.WarehouseContracts.controllers.WarehouseController;
import com.example.WarehouseContracts.dto.forms.WarehousesSearchForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/warehouses")
public class WarehouseControllerImpl implements WarehouseController {


    @Override
    public String warehousePage(
        @Valid @ModelAttribute("form")WarehousesSearchForm form,
        BindingResult bindingResult, Model model) {
        return "";
    }

    @Override
    public String getWarehouse(String id, Model model) {
        return "";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(Integer totalPages, Integer countItemsInCart) {
        return null;
    }
}
