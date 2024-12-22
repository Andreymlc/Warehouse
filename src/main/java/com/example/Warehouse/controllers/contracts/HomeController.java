package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.forms.category.CategorySearchForm;
import com.example.Warehouse.models.forms.product.ProductSearchForm;
import com.example.Warehouse.models.forms.warehouse.WarehousesSearchForm;
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
    @GetMapping("/admin/warehouses")
    String homeAdminWarehousesPage(
        @ModelAttribute("form") WarehousesSearchForm form,
        Model model);

    /**
     * Returns: {@code admin-products.html}
     */
    @PostMapping("/admin")
    String homeAdminPage(
        @ModelAttribute("form") ProductSearchForm form,
        Model model);

    /**
     * Returns: {@code admin-categories.html}
     */
    @PostMapping("/admin/categories")
    String homeAdminCategoriesPage(
        @ModelAttribute("form") CategorySearchForm form,
        Model model);

    /**
     * Returns: {@code home-user.html}
     */
    @GetMapping("/user")
    String homeUserPage(
        @ModelAttribute("form") ProductSearchForm form,
        Model model);

    @GetMapping("/most-popular-products")
    String getFiveMostPopularProducts(Model model);

}
