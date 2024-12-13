package com.example.Warehouse.controllers;

import com.example.Warehouse.services.CategoryService;
import com.example.Warehouse.services.ProductService;
import com.example.Warehouse.services.WarehouseService;
import com.example.WarehouseContracts.controllers.HomeController;
import com.example.WarehouseContracts.dto.forms.cart.AddProductToAdminCartForm;
import com.example.WarehouseContracts.dto.forms.cart.AddProductToUserCartForm;
import com.example.WarehouseContracts.dto.forms.category.CategoryCreateForm;
import com.example.WarehouseContracts.dto.forms.category.CategoryEditForm;
import com.example.WarehouseContracts.dto.forms.category.CategorySearchForm;
import com.example.WarehouseContracts.dto.forms.product.ProductCreateForm;
import com.example.WarehouseContracts.dto.forms.product.ProductsAdminSearchForm;
import com.example.WarehouseContracts.dto.forms.product.ProductsUserSearchForm;
import com.example.WarehouseContracts.dto.forms.warehouse.WarehouseCreateForm;
import com.example.WarehouseContracts.dto.forms.warehouse.WarehousesSearchForm;
import com.example.WarehouseContracts.dto.viewmodels.CategoryViewModel;
import com.example.WarehouseContracts.dto.viewmodels.WarehouseViewModel;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.home.HomeAdminCategoriesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.home.HomeAdminViewModel;
import com.example.WarehouseContracts.dto.viewmodels.home.HomeUserViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.AdminProductsViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductViewModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeControllerImpl implements HomeController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;

    public HomeControllerImpl(
        WarehouseService warehouseService,
        ProductService productService,
        CategoryService categoryService
    ) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
    }

    @Override
    @GetMapping("/admin/warehouses")
    public String homeAdminPage(
        @ModelAttribute("form") WarehousesSearchForm form,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) return "admin-warehouses";

        var warehousesPage = warehouseService.findWarehouses(
            form.pages().substring(),
            form.pages().page(),
            form.pages().size()
        );

        var warehouseViewModels = warehousesPage
            .stream()
            .map(wh -> new WarehouseViewModel(
                    wh.id(),
                    wh.name(),
                    wh.location()
                )
            )
            .toList();

        var viewModel = new HomeAdminViewModel(
            createBaseViewModel(warehousesPage.getTotalPages(), 0),
            warehouseViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("create", new WarehouseCreateForm("", "", form.base()));

        return "admin-warehouses";
    }

    @Override
    @GetMapping("/admin")
    public String homeAdminProductsPage(
        @Valid @ModelAttribute("form") ProductsAdminSearchForm form,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) return "admin-products";

        var productsPage = productService.findProducts(
            form.pages().page(),
            form.pages().size(),
            form.category(),
            form.pages().substring(),
            form.priceSort()
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
        model.addAttribute("add", new AddProductToAdminCartForm(null, form.base()));
        model.addAttribute("createProduct", new ProductCreateForm(null, 0f, null, form.base()));

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

        var categoriesPage = categoryService.findCategories(
            form.pages().substring(),
            form.pages().page(),
            form.pages().size()
        );

        var categoriesViewModels = categoriesPage
            .stream()
            .map(c -> new CategoryViewModel(
                c.id(),
                c.name(),
                c.discount()
            ))
            .toList();

        var viewModel = new HomeAdminCategoriesViewModel(
            createBaseViewModel(categoriesPage.getTotalPages(), 0),
            categoriesViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("create", new CategoryCreateForm(null, 0, form.base()));
        model.addAttribute("edit", new CategoryEditForm(null, null, 0, form.base()));

        return "admin-categories";
    }

    @Override
    @GetMapping("/user")
    public String homeUserPage(
        @Valid @ModelAttribute("form") ProductsUserSearchForm form,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) return "home-user";

        var productsPage = productService.findProducts(
            form.pages().page(),
            form.pages().size(),
            form.category(),
            form.pages().substring(),
            form.priceSort()
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

        var viewModel = new HomeUserViewModel(
            createBaseViewModel(productsPage.getTotalPages(), 0),
            categories,
            productViewModels
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("add", new AddProductToUserCartForm(null, form.base()));

        return "home-user";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
