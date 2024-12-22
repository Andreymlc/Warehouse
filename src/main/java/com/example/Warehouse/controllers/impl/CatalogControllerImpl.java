package com.example.Warehouse.controllers.impl;

import com.example.Warehouse.controllers.contracts.CatalogController;
import com.example.Warehouse.models.dto.category.CategoryAddDto;
import com.example.Warehouse.models.dto.product.ProductAddDto;
import com.example.Warehouse.models.dto.product.ProductEditDto;
import com.example.Warehouse.models.forms.category.CategoryCreateForm;
import com.example.Warehouse.models.forms.category.CategoryEditForm;
import com.example.Warehouse.models.forms.product.ProductCreateForm;
import com.example.Warehouse.models.forms.product.ProductEditForm;
import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import com.example.Warehouse.services.contracts.CategoryService;
import com.example.Warehouse.services.contracts.ProductService;
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
@RequestMapping("/catalog")
public class CatalogControllerImpl implements CatalogController {
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final CategoryService categoryService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    public CatalogControllerImpl(
        ModelMapper modelMapper,
        ProductService productService,
        CategoryService categoryService
    ) {
        this.modelMapper = modelMapper;
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
            LOG.error(bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("createProduct", createProduct);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.createProduct",
                bindingResult
            );

            return "redirect:/home/admin?returnDeleted=false";
        }

        LOG.info("Create product with parameters: {}", createProduct);
        productService.addProduct(modelMapper.map(createProduct, ProductAddDto.class));

        LOG.info("Successful create product with parameters: {}", createProduct);

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @GetMapping("/products/{productId}/delete")
    public String deleteProduct(@PathVariable("productId") String productId) {
        LOG.info("Delete product with id: {}", productId);
        productService.deleteProduct(productId);

        LOG.info("Successful delete product with id: {}", productId);

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @GetMapping("/products/edit")
    public String showEditProduct(
        @ModelAttribute("edit") ProductEditForm edit,
        Model model
    ) {
        LOG.info("Request show the product '{}' editing page", edit.productId());
        model.addAttribute("form", edit);

        return "product-edit";
    }

    @Override
    @PostMapping("/products/edit")
    public String editProduct(
        @Valid @ModelAttribute("edit") ProductEditForm edit,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            LOG.error(bindingResult.getAllErrors());
            model.addAttribute("edit", edit);

            return "product-edit";
        }

        LOG.info("Edit product with id: {}", edit.productId());
        productService.editProduct(modelMapper.map(edit, ProductEditDto.class));

        LOG.info("Successful edit product with id: {}", edit.productId());

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @PostMapping("/categories/create")
    public String createCategory(
        @Valid @ModelAttribute("create") CategoryCreateForm create,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            LOG.error(bindingResult.getAllErrors());

            redirectAttributes.addFlashAttribute("create", create);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.create",
                bindingResult
            );

            return "redirect:/home/admin/categories?returnDeleted=false";
        }

        LOG.info("Create category with parameters: {}", create);
        categoryService.create(new CategoryAddDto(create.name(), create.discount()));

        LOG.info("Successful create category with parameters: {}", create);

        return "redirect:/home/admin/categories?returnDeleted=false";
    }

    @Override
    @GetMapping("/categories/{categoryId}/delete")
    public String deleteCategory(@PathVariable("categoryId") String categoryId) {
        LOG.info("Delete category with id: {}", categoryId);
        categoryService.delete(categoryId);

        LOG.info("Successful delete category with id: {}", categoryId);

        return "redirect:/home/admin/categories?returnDeleted=false";
    }

    @Override
    @GetMapping("/categories/edit")
    public String showEditCategory(
        @ModelAttribute("edit") CategoryEditForm edit,
        Model model
    ) {
        LOG.info("Request show the category '{}' editing page", edit.categoryId());
        model.addAttribute("form", edit);

        return "category-edit";
    }

    @Override
    @PostMapping("/categories/edit")
    public String editCategory(
        @Valid @ModelAttribute("edit") CategoryEditForm edit,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            LOG.error(bindingResult.getAllErrors());
            model.addAttribute("edit", edit);

            return "category-edit";
        }

        LOG.info("Edit category with id: {}", edit.categoryId());
        categoryService.edit(edit.categoryId(), edit.name(), edit.discount());

        LOG.info("Successful edit category with id: {}", edit.categoryId());

        return "redirect:/home/admin/categories?returnDeleted=false";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return null;
    }
}
