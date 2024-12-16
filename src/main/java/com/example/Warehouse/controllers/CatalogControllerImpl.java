package com.example.Warehouse.controllers;

import com.example.Warehouse.controllers.contracts.CatalogController;
import com.example.Warehouse.models.dto.category.CategoryAddDto;
import com.example.Warehouse.models.dto.product.ProductAddDto;
import com.example.Warehouse.models.forms.category.CategoryCreateForm;
import com.example.Warehouse.models.forms.category.CategoryEditForm;
import com.example.Warehouse.models.forms.product.ProductCreateForm;
import com.example.Warehouse.models.forms.product.ProductEditForm;
import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import com.example.Warehouse.services.contracts.CategoryService;
import com.example.Warehouse.services.contracts.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        @Valid @ModelAttribute("createProduct") ProductCreateForm createProduct,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/home/admin?returnDeleted=false";
        }

        productService.addProduct(
            new ProductAddDto(
                createProduct.name(),
                createProduct.category(),
                createProduct.price()
            )
        );

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @GetMapping("/products/{productId}/delete")
    public String deleteProduct(@PathVariable("productId") String productId) {
        productService.deleteProduct(productId);

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @GetMapping("/products/{productId}/edit")
    public String showEditProduct(
        @PathVariable("productId") String productId,
        @Valid @ModelAttribute("edit") ProductEditForm edit,
        Model model
    ) {

        model.addAttribute("form", edit);

        return "product-edit";
    }

    @Override
    @PostMapping("/products/{productId}/edit")
    public String editProduct(
        @PathVariable("productId") String productId,
        @Valid @ModelAttribute("edit") ProductEditForm edit,
        Model model
    ) {

        productService.editProduct(productId, edit.name(), edit.category(), edit.price());

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @PostMapping("/categories/create")
    public String createCategory(
        @Valid @ModelAttribute("create") CategoryCreateForm create,
        BindingResult bindingResult,
        Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "redirect:/home/admin/categories?returnDeleted=false";
        }

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
        model.addAttribute("form", edit);

        return "category-edit";
    }

    @Override
    @PostMapping("/categories/{categoryId}/edit")
    public String editCategory(
        @PathVariable("categoryId") String categoryId,
        @Valid @ModelAttribute("edit") CategoryEditForm edit,
        Model model
    ) {

        categoryService.edit(categoryId, edit.name(), edit.discount());

        return "redirect:/home/admin/categories?returnDeleted=false";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return null;
    }
}
