package com.example.Warehouse.controllers.impl;

import com.example.Warehouse.controllers.contracts.WarehouseController;
import com.example.Warehouse.models.dto.product.ProductMoveDto;
import com.example.Warehouse.models.dto.product.ProductSearchByWarehouseDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseAddDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseSearchDto;
import com.example.Warehouse.models.forms.product.ProductMoveForm;
import com.example.Warehouse.models.forms.product.ProductSetMinMaxForm;
import com.example.Warehouse.models.forms.product.ProductWarehouseSearchForm;
import com.example.Warehouse.models.forms.warehouse.WarehouseCreateForm;
import com.example.Warehouse.models.forms.warehouse.WarehouseEditForm;
import com.example.Warehouse.models.forms.warehouse.WarehousesSearchForm;
import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductStockViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductsStockViewModel;
import com.example.Warehouse.models.viewmodels.warehouse.WarehouseViewModel;
import com.example.Warehouse.models.viewmodels.warehouse.WarehousesViewModel;
import com.example.Warehouse.services.contracts.CategoryService;
import com.example.Warehouse.services.contracts.ProductService;
import com.example.Warehouse.services.contracts.StockService;
import com.example.Warehouse.services.contracts.WarehouseService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/warehouses")
public class WarehouseControllerImpl implements WarehouseController {
    private final ModelMapper modelMapper;
    private final StockService stockService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

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
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("create", create);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.create",
                bindingResult
            );

            return "redirect:/warehouses?returnDeleted=false";
        }

        LOG.info("Create warehouse with parameters: {}", create);
        warehouseService.add(new WarehouseAddDto(create.name(), create.location()));

        LOG.info("Successful create warehouse with parameters: {}", create);

        return "redirect:/warehouses?returnDeleted=false";
    }

    @Override
    @GetMapping
    public String homeAdminWarehousesPage(
        @ModelAttribute("form") WarehousesSearchForm form,
        Model model
    ) {
        LOG.info("Find warehouses with search parameters: {}", form);
        var warehousesPage = warehouseService
            .findWarehouses(modelMapper.map(form, WarehouseSearchDto.class));

        var warehouseViewModels = warehousesPage
            .stream()
            .map(warehouseDto -> modelMapper.map(warehouseDto, WarehouseViewModel.class))
            .toList();

        var viewModel = new WarehousesViewModel(
            createBaseViewModel(warehousesPage.getTotalPages(), 0),
            warehouseViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        if (!model.containsAttribute("create")) model.addAttribute("create", new WarehouseCreateForm(null, null));
        if (!model.containsAttribute("edit")) model.addAttribute("edit", new WarehouseEditForm(null, null, null));

        LOG.info("Returning 'admin-warehouses' view with warehouses data");

        return "admin-warehouses";
    }

    @Override
    @GetMapping("/{warehouseId}/manage")
    public String showManagePage(
        @PathVariable("warehouseId") String warehouseId,
        @ModelAttribute("form") ProductWarehouseSearchForm form,
        Model model
    ) {
        LOG.info("Admin requests warehouse ('{}') manage page", warehouseId);
        var productsPage = productService
            .findProductsByWarehouse(modelMapper.map(form, ProductSearchByWarehouseDto.class));

        var categories = categoryService.findAllNamesCategories(form.returnDeleted());

        var productViewModels = productsPage
            .stream()
            .map(productStockDto -> modelMapper.map(productStockDto, ProductStockViewModel.class))
            .toList();

        var viewModel = new ProductsStockViewModel(
            createBaseViewModel(productsPage.getTotalPages(), 0),
            categories,
            productViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        if (!model.containsAttribute("minimum"))
            model.addAttribute("minimum", new ProductSetMinMaxForm(null, null, "", warehouseId));
        if (!model.containsAttribute("maximum"))
            model.addAttribute("maximum", new ProductSetMinMaxForm(null, null, "", warehouseId));
        if (!model.containsAttribute("moveForm"))
            model.addAttribute("moveForm", new ProductMoveForm(null, null, null, warehouseId, null));

        LOG.info("Returning 'warehouse' view with product data");

        return "warehouse";
    }

    @Override
    @PostMapping("/move-product")
    public String move(
        @Valid @ModelAttribute("moveForm") ProductMoveForm moveForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("moveForm", moveForm);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.moveForm",
                bindingResult
            );

            return "redirect:/warehouses/" + moveForm.warehouseId() + "/manage?returnDeleted=false";
        }

        LOG.info(
            "Move product {} from {} to {}",
            moveForm.productId(), moveForm.warehouseId(), moveForm.newWarehouseId()
        );
        stockService.moveProduct(modelMapper.map(moveForm, ProductMoveDto.class));

        LOG.info("Successful move product");

        return "redirect:/warehouses/" + moveForm.warehouseId() + "/manage?returnDeleted=false";
    }

    @Override
    @PostMapping("/set-minimum")
    public String setMinimum(
        @Valid @ModelAttribute("minimum") ProductSetMinMaxForm editForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("minimum", editForm.warehouseId());
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.minimum",
                bindingResult
            );

            return "redirect:/warehouses/" + editForm.warehouseId() + "/manage?returnDeleted=false";
        }

        LOG.info(
            "Set minimum ({}) for product '{}' in warehouse {}",
            editForm.value(), editForm.productId(), editForm.warehouseId()
        );

        stockService.setMinimum(editForm.value(), editForm.productId(), editForm.warehouseId());

        LOG.info("Successful set minimum for product");

        return "redirect:/warehouses/" + editForm.warehouseId() + "/manage?returnDeleted=false";
    }

    @Override
    @PostMapping("/set-maximum")
    public String setMaximum(
        @Valid @ModelAttribute("maximum") ProductSetMinMaxForm editForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("maximum", editForm.warehouseId());
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.maximum",
                bindingResult
            );

            return "redirect:/warehouses/" + editForm.warehouseId() + "/manage?returnDeleted=false";
        }

        LOG.info(
            "Set maximum ({}) for product '{}' in warehouse {}",
            editForm.value(), editForm.productId(), editForm.warehouseId()
        );

        stockService.setMaximum(editForm.value(), editForm.productId(), editForm.warehouseId());

        LOG.info("Successful set maximum for product");

        return "redirect:/warehouses/" + editForm.warehouseId() + "/manage?returnDeleted=false";
    }

    @Override
    @GetMapping("/{warehouseId}/delete")
    public String delete(@PathVariable("warehouseId") String warehouseId) {
        LOG.info("Delete warehouse with id: {}", warehouseId);
        warehouseService.delete(warehouseId);

        LOG.info("Successful delete category with id: {}", warehouseId);

        return "redirect:/warehouses?returnDeleted=false";
    }

    @GetMapping("/edit")
    public String showEdit(
        @ModelAttribute("edit") WarehouseEditForm edit,
        Model model
    ) {
        LOG.info("Request show the warehouse '{}' editing page", edit.warehouseId());
        model.addAttribute("form", edit);

        return "warehouse-edit";
    }

    @Override
    @PostMapping("/edit")
    public String edit(
        @Valid @ModelAttribute("edit") WarehouseEditForm edit,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", edit);

            return "warehouse-edit";
        }

        LOG.info("Edit category with id: {}", edit.warehouseId());

        warehouseService.edit(edit.warehouseId(), edit.name(), edit.location());

        LOG.info("Successful edit category with id: {}", edit.warehouseId());

        return "redirect:/warehouses?returnDeleted=false";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
