package com.example.Warehouse.controllers;

import com.example.WarehouseContracts.dto.WarehouseAddDto;
import com.example.WarehouseContracts.dto.forms.warehouse.WarehouseCreateForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.example.Warehouse.services.StockService;
import org.springframework.validation.BindingResult;
import com.example.Warehouse.services.ProductService;
import com.example.Warehouse.services.CategoryService;
import com.example.Warehouse.services.WarehouseService;
import com.example.WarehouseContracts.dto.forms.base.BaseAdminForm;
import com.example.WarehouseContracts.controllers.WarehouseController;
import com.example.WarehouseContracts.dto.forms.product.ProductMoveForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductViewModel;
import com.example.WarehouseContracts.dto.forms.warehouse.WarehouseDeleteForm;
import com.example.WarehouseContracts.dto.forms.product.ProductWarehouseSearchForm;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductSetMinimumForm;
import com.example.WarehouseContracts.dto.viewmodels.product.AdminProductsViewModel;

@Controller
@RequestMapping("/warehouses")
public class WarehouseControllerImpl implements WarehouseController {
    private final StockService stockService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;

    public WarehouseControllerImpl(
        StockService stockService,
        ProductService productService,
        CategoryService categoryService,
        WarehouseService warehouseService) {
        this.stockService = stockService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
    }

    @Override
    @GetMapping("/{warehouseId}/manage")
    public String showManagePage(
        @PathVariable("warehouseId") String warehouseId,
        @ModelAttribute("form") ProductWarehouseSearchForm form,
        Model model) {

        var productsPage = productService.getProductsByWarehouse(
            form.pages().substring(),
            form.pages().page(),
            form.pages().size(),
            form.category(),
            warehouseId
        );

        var categories = categoryService.getAllNameCategories();

        var productViewModels = productsPage
            .stream()
            .map(p -> new ProductViewModel(
                    p.id(),
                    p.name(),
                    p.category(),
                    p.quantity(),
                    p.price(),
                    p.oldPrice()
                )
            )
            .toList();

        var viewModel = new AdminProductsViewModel(
            createBaseViewModel(productsPage.getTotalPages(), 0),
            categories,
            productViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("editForm", new ProductSetMinimumForm(null, null, "", "", new BaseAdminForm(form.base().role(), form.base().userName())));
        model.addAttribute("moveForm", new ProductMoveForm(null, null, null, new BaseAdminForm(form.base().role(), form.base().userName()), null, null));

        return "warehouse";
    }

    @Override
    @PostMapping("/{warehouseId}/move")
    public String move(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("moveForm") ProductMoveForm moveForm,
        BindingResult bindingResult,
        Model model) {

        stockService.moveProduct(moveForm.productId(), warehouseId, moveForm.newWarehouseId(), moveForm.countItems());

        return "redirect:/warehouses/" + warehouseId + "/manage?" +
            "base.userName=" + moveForm.base().userName() +
            "&base.role=" + moveForm.base().role() +
            "&priceSort=true" +
            "&warehouseId=" + warehouseId;
    }

    @Override
    @PostMapping("/{warehouseId}/set-minimum")
    public String setMinimum(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("editForm")ProductSetMinimumForm editForm,
        BindingResult bindingResult,
        Model model) {

        stockService.setMinimum(editForm.minimum(), editForm.productId(), warehouseId);

        return "redirect:/warehouses/" + warehouseId + "/manage?" +
            "base.userName=" + editForm.base().userName() +
            "&base.role=" + editForm.base().role() +
            "&priceSort=true" +
            "&warehouseId=" + warehouseId;
    }

    @Override
    @GetMapping("/{warehouseId}/delete")
    public String delete(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("form") WarehouseDeleteForm form,
        BindingResult bindingResult) {

        warehouseService.delete(warehouseId);

        return "redirect:/home/admin/warehouses?" +
            "base.userName=" + form.base().userName() +
            "&base.role=" + form.base().role();
    }

    @PostMapping("/create")
    public String create(
        @Valid @ModelAttribute("create")WarehouseCreateForm create,
        BindingResult bindingResult,
        Model model) {

        warehouseService.add(new WarehouseAddDto(create.name(), create.location()));

        return "redirect:/home/admin/warehouses?" +
            "base.userName=" + create.base().userName() +
            "&base.role=" + create.base().role();
    }

    @Override
    public BasePagesViewModel createBaseViewModel(Integer totalPages, Integer countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
