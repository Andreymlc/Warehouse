package com.example.Warehouse.controllers;

import com.example.Warehouse.services.CategoryService;
import com.example.WarehouseContracts.dto.ProductAddDto;
import com.example.WarehouseContracts.dto.forms.category.CategorySetDiscountForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import com.example.Warehouse.services.ProductService;
import com.example.WarehouseContracts.controllers.ProductController;
import com.example.WarehouseContracts.dto.forms.product.ProductMoveForm;
import com.example.WarehouseContracts.dto.forms.product.ProductDeleteForm;
import com.example.WarehouseContracts.dto.forms.product.ProductCreateForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductSetMinimumForm;

@Controller
@RequestMapping("/products")
public class ProductControllerImpl implements ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductControllerImpl(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    public String move(ProductMoveForm form, BindingResult bindingResult, Model model) {
        return "";
    }

    @Override
    public String setMinimum(ProductSetMinimumForm form, BindingResult bindingResult, Model model) {
        return "";
    }

    @Override
    @PostMapping("/create")
    public String create(
        @Valid @ModelAttribute("create")ProductCreateForm create,
        BindingResult bindingResult,
        Model model) {

        productService.addProduct(new ProductAddDto(create.name(), create.category(), create.price()));

        return "redirect:/home/admin/products?substring=&priceSort=true&" +
            "base.userName=" + create.base().userName() +
            "&base.role=" + create.base().role();
    }

    @Override
    @GetMapping("/{id}/delete")
    public String delete(
        @PathVariable("id") String id,
        @Valid @ModelAttribute("form") ProductDeleteForm form,
        BindingResult bindingResult) {

        productService.deleteProduct(id);
        return "redirect:/home/admin/products?substring=&priceSort=true&" +
            "base.userName=" + form.base().userName() +
            "&base.role=" + form.base().role();
    }

    @Override
    @PostMapping("/{categoryId}/set-discount")
    public String setDiscount(
        @PathVariable("categoryId") String categoryId,
        @Valid @ModelAttribute("discount") CategorySetDiscountForm discount,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("discount", discount);
            return "admin-categories";
        }

        categoryService.setDiscount(categoryId, discount.discount());

        return "redirect:/home/admin/categories?substring=&priceSort=true&" +
            "base.userName=" + discount.base().userName() +
            "&base.role=" + discount.base().role();
    }

    @Override
    public BasePagesViewModel createBaseViewModel(Integer totalPages, Integer countItemsInCart) {
        return null;
    }
}
