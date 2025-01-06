package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.forms.product.ProductMoveForm;
import com.example.Warehouse.models.forms.product.ProductSetMinMaxForm;
import com.example.Warehouse.models.forms.product.ProductWarehouseSearchForm;
import com.example.Warehouse.models.forms.warehouse.WarehouseCreateForm;
import com.example.Warehouse.models.forms.warehouse.WarehouseEditForm;
import com.example.Warehouse.models.forms.warehouse.WarehousesSearchForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/warehouses")
public interface WarehouseController extends BaseController {

    @PostMapping("/create")
    String create(
        @Valid @ModelAttribute("create") WarehouseCreateForm create,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model);

    @GetMapping
    String homeAdminWarehousesPage(
        @ModelAttribute("form") WarehousesSearchForm form,
        Model model);

    @PostMapping("/edit")
    String edit(
        @Valid @ModelAttribute("edit") WarehouseEditForm edit,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/{warehouseId}/manage")
    String showManagePage(
        @PathVariable("warehouseId") String warehouseId,
        @ModelAttribute("form") ProductWarehouseSearchForm form,
        Model model);

    @PostMapping("/move")
    String move(
        @Valid @ModelAttribute("form") ProductMoveForm form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model);

    @PostMapping("/set-minimum")
    String setMinimum(
        @Valid @ModelAttribute("editForm") ProductSetMinMaxForm editForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model);

    @PostMapping("/set-maximum")
    String setMaximum(
        @Valid @ModelAttribute("maximum") ProductSetMinMaxForm editForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model);

    @GetMapping("/{warehouseId}/delete")
    String delete(@PathVariable("warehouseId") String warehouseId);
}
