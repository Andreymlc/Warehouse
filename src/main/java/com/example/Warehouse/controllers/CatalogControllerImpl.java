package com.example.Warehouse.controllers;

import com.example.Warehouse.dto.category.CategoryAddDto;
import com.example.Warehouse.dto.product.ProductAddDto;
import com.example.Warehouse.services.contracts.CategoryService;
import com.example.Warehouse.services.contracts.ProductService;
import com.example.WarehouseContracts.controllers.CatalogController;
import com.example.WarehouseContracts.dto.forms.category.CategoryCreateForm;
import com.example.WarehouseContracts.dto.forms.category.CategoryEditForm;
import com.example.WarehouseContracts.dto.forms.product.ProductCreateForm;
import com.example.WarehouseContracts.dto.forms.product.ProductEditForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.utils.validation.category.ExistingCategory;
import com.example.WarehouseContracts.utils.validation.product.ExistProduct;
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

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @GetMapping("/products/{productId}/delete")
    public String deleteProduct(
        @Valid @ExistProduct @PathVariable("productId") String productId,
        BindingResult bindingResult
    ) {
        productService.deleteProduct(productId);

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @GetMapping("/products/{productId}/edit")
    public String showEditProduct(
        @Valid @ExistingCategory @PathVariable("productId") String categoryId,
        @Valid @ModelAttribute("edit") ProductEditForm form,
        BindingResult bindingResult
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

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @PostMapping("/categories/create")
    public String createCategory(
        @Valid @ModelAttribute("create") CategoryCreateForm create,
        BindingResult bindingResult,
        Model model
    ) {
        categoryService.create(new CategoryAddDto(create.name(), create.discount()));

        return "redirect:/home/admin/categories?returnDeleted=false";
    }

    @Override
    @GetMapping("/categories/{categoryId}/delete")
    public String deleteCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.delete(categoryId);

        return "redirect:/home/admin/categories?returnDeleted=false";
    }

    @Override
    @GetMapping("/categories/{categoryId}/edit")
    public String showEditCategory(
        @PathVariable("categoryId") String categoryId,
        @Valid @ModelAttribute("edit") CategoryEditForm edit,
        Model model
    ) {
        model.addAttribute("edit", edit);

        return "category-edit";
    }

    @Override
    @PostMapping("/categories/{categoryId}/edit")
    public String editCategory(
        @PathVariable("categoryId") String categoryId,
        @Valid @ModelAttribute("edit") CategoryEditForm edit,
        BindingResult bindingResult
    ) {
        categoryService.edit(categoryId, edit.name(), edit.discount());

        return "redirect:/home/admin/categories?returnDeleted=false";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return null;
    }
}
