package com.example.Warehouse.controllers;

import com.example.Warehouse.dto.WarehouseAddDto;
import com.example.Warehouse.services.CategoryService;
import com.example.Warehouse.services.ProductService;
import com.example.Warehouse.services.StockService;
import com.example.Warehouse.services.WarehouseService;
import com.example.Warehouse.utils.UrlUtil;
import com.example.WarehouseContracts.controllers.WarehouseController;
import com.example.WarehouseContracts.dto.forms.base.BaseForm;
import com.example.WarehouseContracts.dto.forms.product.ProductMoveForm;
import com.example.WarehouseContracts.dto.forms.product.ProductSetMinMaxForm;
import com.example.WarehouseContracts.dto.forms.product.ProductWarehouseSearchForm;
import com.example.WarehouseContracts.dto.forms.warehouse.WarehouseCreateForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.AdminProductsViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductViewModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        WarehouseService warehouseService
    ) {
        this.stockService = stockService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
    }

    @PostMapping("/create")
    public String create(
        @Valid @ModelAttribute("create") WarehouseCreateForm create,
        BindingResult bindingResult,
        Model model
    ) {
        warehouseService.add(new WarehouseAddDto(create.name(), create.location()));

        return "redirect:" + UrlUtil.warehousesUrl(create.base());
    }

    @Override
    @GetMapping("/{warehouseId}/delete")
    public String delete(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("form") BaseForm form,
        BindingResult bindingResult
    ) {
        warehouseService.delete(warehouseId);

        return "redirect:" + UrlUtil.warehousesUrl(form);
    }

    @Override
    @GetMapping("/{warehouseId}/manage")
    public String showManagePage(
        @PathVariable("warehouseId") String warehouseId,
        @ModelAttribute("form") ProductWarehouseSearchForm form,
        Model model
    ) {
        var productsPage = productService.findProductsByWarehouse(
            form.pages().page(),
            form.pages().size(),
            form.category(),
            form.pages().substring(),
            warehouseId
        );

        var categories = categoryService.findAllNamesCategories();

        var productViewModels = productsPage
            .stream()
            .map(p -> new ProductViewModel(
                    p.id(),
                    p.name(),
                    p.price(),
                    p.oldPrice(),
                    p.category(),
                    p.quantity()
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
        model.addAttribute("minimum", new ProductSetMinMaxForm(null, null, "", "", form.base()));
        model.addAttribute("maximum", new ProductSetMinMaxForm(null, null, "", "", form.base()));
        model.addAttribute("moveForm", new ProductMoveForm(null, null, null, form.base(), null, null));

        return "warehouse";
    }

    @Override
    @PostMapping("/{warehouseId}/move")
    public String move(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("moveForm") ProductMoveForm moveForm,
        BindingResult bindingResult,
        Model model
    ) {
        stockService.moveProduct(moveForm.productId(), warehouseId, moveForm.newWarehouseId(), moveForm.countItems());

        return "redirect:" + UrlUtil.warehouseManageUrl(moveForm.base(), warehouseId);
    }

    @Override
    @PostMapping("/{warehouseId}/set-minimum")
    public String setMinimum(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("minimum") ProductSetMinMaxForm editForm,
        BindingResult bindingResult,
        Model model
    ) {
        stockService.setMinimum(editForm.value(), editForm.productId(), warehouseId);

        return "redirect:" + UrlUtil.warehouseManageUrl(editForm.base(), warehouseId);
    }

    @Override
    @PostMapping("/{warehouseId}/set-maximum")
    public String setMaximum(
        @PathVariable("warehouseId") String warehouseId,
        @Valid @ModelAttribute("maximum") ProductSetMinMaxForm editForm,
        BindingResult bindingResult,
        Model model
    ) {
        stockService.setMaximum(editForm.value(), editForm.productId(), warehouseId);

        return "redirect:" + UrlUtil.warehouseManageUrl(editForm.base(), warehouseId);
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
