package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.forms.warehouse.WarehouseCreateForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import com.example.Warehouse.models.forms.product.ProductMoveForm;
import com.example.Warehouse.models.forms.product.ProductSetMinMaxForm;
import com.example.Warehouse.models.forms.product.ProductWarehouseSearchForm;

@RequestMapping("/warehouses")
public interface WarehouseController extends BaseController {

    @PostMapping("/create")
    String create(
        @Valid @ModelAttribute("create") WarehouseCreateForm create,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/{warehouseId}/manage")
    String showManagePage(
        @PathVariable("warehouseId") String warehouseId,
        @ModelAttribute("form") ProductWarehouseSearchForm form,
        Model model);

    @PostMapping("/{warehouseId}/move")
    String move(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("form") ProductMoveForm form,
        BindingResult bindingResult,
        Model model);

    @PostMapping("/{warehouseId}/set-minimum")
    String setMinimum(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("editForm") ProductSetMinMaxForm editForm,
        BindingResult bindingResult,
        Model model);

    @PostMapping("/{warehouseId}/set-maximum")
    String setMaximum(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("maximum") ProductSetMinMaxForm editForm,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/{warehouseId}/delete")
    String delete(@PathVariable("warehouseId") String warehouseId);
}
