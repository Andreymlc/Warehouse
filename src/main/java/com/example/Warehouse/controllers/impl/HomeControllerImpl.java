package com.example.Warehouse.controllers.impl;

import com.example.Warehouse.models.dto.category.CategorySearchDto;
import com.example.Warehouse.models.dto.product.ProductSearchDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseSearchDto;
import com.example.Warehouse.services.contracts.CategoryService;
import com.example.Warehouse.services.contracts.ProductService;
import com.example.Warehouse.services.contracts.WarehouseService;
import com.example.Warehouse.controllers.contracts.HomeController;
import com.example.Warehouse.models.forms.cart.AddProductToAdminCartForm;
import com.example.Warehouse.models.forms.cart.AddProductToUserCartForm;
import com.example.Warehouse.models.forms.category.CategoryCreateForm;
import com.example.Warehouse.models.forms.category.CategoryEditForm;
import com.example.Warehouse.models.forms.category.CategorySearchForm;
import com.example.Warehouse.models.forms.product.ProductCreateForm;
import com.example.Warehouse.models.forms.product.ProductSearchForm;
import com.example.Warehouse.models.forms.warehouse.WarehouseCreateForm;
import com.example.Warehouse.models.forms.warehouse.WarehousesSearchForm;
import com.example.Warehouse.models.viewmodels.category.CategoryViewModel;
import com.example.Warehouse.models.viewmodels.warehouse.WarehouseViewModel;
import com.example.Warehouse.models.viewmodels.warehouse.WarehousesViewModel;
import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import com.example.Warehouse.models.viewmodels.category.CategoriesViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductsViewModel;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @GetMapping("/admin/warehouses")
    public String homeAdminPage(
        @Valid @ModelAttribute("form") WarehousesSearchForm form,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) return "admin-warehouses";

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
        model.addAttribute("create", new WarehouseCreateForm("", ""));

        return "admin-warehouses";
    }

    @Override
    @GetMapping("/admin")
    public String homeAdminProductsPage(
        @Valid @ModelAttribute("form") ProductSearchForm form,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) return "admin-products";

        LOG.info("Fetching products with search parameters: {}", form);

        var productsPage = productService
            .findProducts(modelMapper.map(form, ProductSearchDto.class));

        LOG.info("Found {} products", productsPage.getTotalElements());

        var categories = categoryService.findAllNamesCategories(form.returnDeleted());

        LOG.info("Fetched {} categories", categories.size());

        var productViewModels = productsPage
            .stream()
            .map(productDto -> modelMapper.map(productDto, ProductViewModel.class))
            .toList();

        var viewModel = new ProductsViewModel(
            createBaseViewModel(productsPage.getTotalPages(), 0),
            categories,
            productViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("add", new AddProductToAdminCartForm(null));
        model.addAttribute("createProduct", new ProductCreateForm(null, 0f, null));

        LOG.info("Returning 'admin-products' view with product data and categories.");

        return "admin-products";
    }

    @Override
    @GetMapping("/admin/categories")
    public String homeAdminCategoriesPage(
        @Valid @ModelAttribute("form") CategorySearchForm form,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "admin-categories";
        }

        var categoriesPage = categoryService
            .findCategories(modelMapper.map(form, CategorySearchDto.class));

        var categoriesViewModels = categoriesPage
            .stream()
            .map(categoryDto -> modelMapper.map(categoryDto, CategoryViewModel.class))
            .toList();

        var viewModel = new CategoriesViewModel(
            createBaseViewModel(categoriesPage.getTotalPages(), 0),
            categoriesViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("create", new CategoryCreateForm(null, 0));
        model.addAttribute("edit", new CategoryEditForm(null, null, 0));

        return "admin-categories";
    }

    @Override
    @GetMapping("/user")
    public String homeUserPage(
        @Valid @ModelAttribute("form") ProductSearchForm form,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) return "home-user";

        var productsPage = productService
            .findProducts(modelMapper.map(form, ProductSearchDto.class));

        var categories = categoryService.findAllNamesCategories(false);

        var productViewModels = productsPage
            .stream()
            .map(productDto -> modelMapper.map(productDto, ProductViewModel.class))
            .toList();

        var viewModel = new ProductsViewModel(
            createBaseViewModel(productsPage.getTotalPages(), 0),
            categories,
            productViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("add", new AddProductToUserCartForm(null));

        return "home-user";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
