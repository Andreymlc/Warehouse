package com.example.Warehouse.controllers;

import com.example.Warehouse.dto.product.ProductSearchByWarehouseDto;
import com.example.Warehouse.dto.warehouse.WarehouseAddDto;
import com.example.Warehouse.services.contracts.CategoryService;
import com.example.Warehouse.services.contracts.ProductService;
import com.example.Warehouse.services.contracts.StockService;
import com.example.Warehouse.services.contracts.WarehouseService;
import com.example.WarehouseContracts.controllers.WarehouseController;
import com.example.WarehouseContracts.dto.forms.product.ProductMoveForm;
import com.example.WarehouseContracts.dto.forms.product.ProductSetMinMaxForm;
import com.example.WarehouseContracts.dto.forms.product.ProductWarehouseSearchForm;
import com.example.WarehouseContracts.dto.forms.warehouse.WarehouseCreateForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductStockViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductsStockViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductsViewModel;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/warehouses")
public class WarehouseControllerImpl implements WarehouseController {
    private final ModelMapper modelMapper;
    private final StockService stockService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;

    public WarehouseControllerImpl(
        ModelMapper modelMapper,
        StockService stockService,
        ProductService productService,
        CategoryService categoryService,
        WarehouseService warehouseService
    ) {
        this.modelMapper = modelMapper;
        this.stockService = stockService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
    }

    @Override
    @PostMapping("/create")
    public String create(
        @Valid @ModelAttribute("create") WarehouseCreateForm create,
        BindingResult bindingResult,
        Model model
    ) {
        warehouseService.add(new WarehouseAddDto(create.name(), create.location()));

        return "redirect:/home/admin/warehouses?returnDeleted=false";
    }

    @Override
    @GetMapping("/{warehouseId}/delete")
    public String delete(@PathVariable("warehouseId") String warehouseId) {
        warehouseService.delete(warehouseId);

        return "redirect:/home/admin/warehouses?returnDeleted=false";
    }

    @Override
    @GetMapping("/{warehouseId}/manage")
    public String showManagePage(
        @PathVariable("warehouseId") String warehouseId,
        @ModelAttribute("form") ProductWarehouseSearchForm form,
        Model model
    ) {
        var productsPage = productService
            .findProductsByWarehouse(modelMapper.map(form, ProductSearchByWarehouseDto.class)).toPage();

        var categories = categoryService.findAllNamesCategories(form.returnDeleted());

        var productViewModels = productsPage
            .stream()
            .map(p -> new ProductStockViewModel(
                    p.id(),
                    p.name(),
                    p.quantity(),
                    p.minStock(),
                    p.maxStock(),
                    p.category(),
                    p.isDeleted()
                )
            )
            .toList();

        var viewModel = new ProductsStockViewModel(
            createBaseViewModel(productsPage.getTotalPages(), 0),
            categories,
            productViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("minimum", new ProductSetMinMaxForm(null, null, "", ""));
        model.addAttribute("maximum", new ProductSetMinMaxForm(null, null, "", ""));
        model.addAttribute("moveForm", new ProductMoveForm(null, null, null, null, null));

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

        return "redirect:/warehouses/" + warehouseId + "/manage?returnDeleted=false";
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

        return "redirect:/warehouses/" + warehouseId + "/manage?returnDeleted=false";
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

        return "redirect:/warehouses/" + warehouseId + "/manage?returnDeleted=false";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
