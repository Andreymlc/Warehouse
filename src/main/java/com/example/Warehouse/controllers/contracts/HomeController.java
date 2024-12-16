package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.forms.category.CategorySearchForm;
import com.example.Warehouse.models.forms.product.ProductSearchForm;
import com.example.Warehouse.models.forms.warehouse.WarehousesSearchForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/home")
public interface HomeController extends BaseController {

    /**
     * Returns: {@code admin-warehouses.html}
     */
    @GetMapping("/admin")
    String homeAdminPage(
        @ModelAttribute("form") WarehousesSearchForm form,
        BindingResult bindingResult,
        Model model);


    /**
     * Returns: {@code admin-products.html}
     */
    @PostMapping("/admin/products")
    String homeAdminProductsPage(
        @Valid @ModelAttribute("form") ProductSearchForm form,
        BindingResult bindingResult,
        Model model);

    /**
     * Returns: {@code admin-categories.html}
     */
    @PostMapping("/admin/categories")
    String homeAdminCategoriesPage(
        @Valid @ModelAttribute("form") CategorySearchForm form,
        BindingResult bindingResult,
        Model model);

    /**
     * Returns: {@code home-user.html}
     */
    @GetMapping("/user")
    String homeUserPage(
        @ModelAttribute("form") ProductSearchForm form,
        BindingResult bindingResult,
        Model model);

}
