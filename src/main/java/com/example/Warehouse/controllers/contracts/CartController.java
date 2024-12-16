package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.forms.cart.AddProductToAdminCartForm;
import com.example.Warehouse.models.forms.cart.AddProductToUserCartForm;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface CartController extends BaseController {
    @GetMapping("/user")
    String userCartPage(
        Authentication authentication,
        Model model);

    @GetMapping("/admin")
    String adminCartPage(
        Authentication authentication,
        Model model);

    @PostMapping("/user/add-product")
    String addProductToUserCart(
        Authentication authentication,
        @Valid @ModelAttribute("add") AddProductToUserCartForm add,
        BindingResult bindingResult,
        Model model);

    @PostMapping("/admin/add-product")
    String addProductToAdminCart(
        Authentication authentication,
        @Valid @ModelAttribute("add") AddProductToAdminCartForm add,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/user/delete-product/{productId}")
    String deleteProductFromUserCart(
        Authentication authentication,
        @PathVariable("productId") String productId,
        Model model);

    @GetMapping("/admin/delete-product/{productId}")
    String deleteProductFromAdminCart(
        Authentication authentication,
        @PathVariable("productId") String productId,
        Model model);
}
