package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.forms.product.ProductEditForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import com.example.Warehouse.models.forms.category.CategoryEditForm;
import com.example.Warehouse.models.forms.product.ProductCreateForm;
import com.example.Warehouse.models.forms.category.CategoryCreateForm;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/catalog")
public interface CatalogController extends BaseController {

    @PostMapping("/products/create")
    String createProduct(
        @Valid @ModelAttribute("createProduct") ProductCreateForm create,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model);

    @GetMapping("/products/{productId}/delete")
    String deleteProduct(@PathVariable("productId") String productId);

    @GetMapping("/products/edit")
    String showEditProduct(
        @ModelAttribute("edit") ProductEditForm form,
        Model model);

    @PostMapping("/products/edit")
    String editProduct(
        @Valid @ModelAttribute("edit") ProductEditForm form,
        BindingResult bindingResult,
        Model model);

    @PostMapping("/categories/create")
    String createCategory(
        @Valid @ModelAttribute("create") CategoryCreateForm create,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model);

    @GetMapping("/categories/{categoryId}/delete")
    String deleteCategory(@PathVariable("categoryId") String categoryId);

    @GetMapping("/categories/edit")
    String showEditCategory(
        @ModelAttribute("edit") CategoryEditForm edit,
        Model model);

    @PostMapping("/categories/edit")
    String editCategory(
        @Valid @ModelAttribute("edit") CategoryEditForm edit,
        BindingResult bindingResult,
        Model model);

}
