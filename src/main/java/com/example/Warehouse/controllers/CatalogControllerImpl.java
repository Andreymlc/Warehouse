package com.example.Warehouse.controllers;

import com.example.Warehouse.dto.CategoryAddDto;
import com.example.Warehouse.dto.ProductAddDto;
import com.example.Warehouse.services.CategoryService;
import com.example.Warehouse.services.ProductService;
import com.example.Warehouse.utils.UrlUtil;
import com.example.WarehouseContracts.controllers.CatalogController;
import com.example.WarehouseContracts.dto.forms.base.BaseForm;
import com.example.WarehouseContracts.dto.forms.category.CategoryCreateForm;
import com.example.WarehouseContracts.dto.forms.category.CategoryEditForm;
import com.example.WarehouseContracts.dto.forms.product.ProductCreateForm;
import com.example.WarehouseContracts.dto.forms.product.ProductEditForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/catalog")
public class CatalogControllerImpl implements CatalogController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public CatalogControllerImpl(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    @PostMapping("/products/create")
    public String createProduct(
        @Valid @ModelAttribute("createProduct") ProductCreateForm create,
        BindingResult bindingResult,
        Model model
    ) {
        productService.addProduct(
            new ProductAddDto(
                create.name(),
                create.category(),
                create.price()
            )
        );

        return "redirect:" + UrlUtil.homeUrl(create.base());
    }

    @Override
    @GetMapping("/products/{productId}/delete")
    public String deleteProduct(
        @PathVariable("productId") String productId,
        @Valid @ModelAttribute("form") BaseForm form,
        BindingResult bindingResult
    ) {
        productService.deleteProduct(productId);

        return "redirect:" + UrlUtil.homeUrl(form);
    }

    @Override
    @GetMapping("/products/{productId}/edit")
    public String showEditProduct(
        @PathVariable("productId") String categoryId,
        @Valid @ModelAttribute("edit") ProductEditForm form,
        Model model
    ) {
        model.addAttribute("form", form);

        return "product-edit";
    }

    @Override
    @PostMapping("/products/{productId}/edit")
    public String editProduct(
        @PathVariable("productId") String productId,
        @Valid @ModelAttribute("edit") ProductEditForm form,
        BindingResult bindingResult
    ) {

        productService.editProduct(productId, form.name(), form.category(), form.price());

        return "redirect:" + UrlUtil.homeUrl(form.base());
    }

    @Override
    @PostMapping("/categories/create")
    public String createCategory(
        @Valid @ModelAttribute("create") CategoryCreateForm create,
        BindingResult bindingResult,
        Model model
    ) {
        categoryService.addCategory(new CategoryAddDto(create.name(), create.discount()));

        return "redirect:" + UrlUtil.categoriesUrl(create.base());
    }

    @Override
    @GetMapping("/categories/{categoryId}/delete")
    public String deleteCategory(
        @PathVariable("categoryId") String categoryId,
        @Valid @ModelAttribute("form") BaseForm form,
        BindingResult bindingResult
    ) {
        categoryService.deleteCategory(categoryId);

        return "redirect:" + UrlUtil.categoriesUrl(form);
    }

    @Override
    @GetMapping("/categories/{categoryId}/edit")
    public String showEditCategory(
        @PathVariable("categoryId") String categoryId,
        @Valid @ModelAttribute("edit") CategoryEditForm form,
        Model model
    ) {
        model.addAttribute("form", form);

        return "category-edit";
    }

    @Override
    @PostMapping("/categories/{categoryId}/edit")
    public String editCategory(
        @PathVariable("categoryId") String categoryId,
        @Valid @ModelAttribute("edit") CategoryEditForm edit,
        BindingResult bindingResult
    ) {
        categoryService.editCategory(categoryId, edit.name(), edit.discount());

        return "redirect:" + UrlUtil.categoriesUrl(edit.base());
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return null;
    }
}
