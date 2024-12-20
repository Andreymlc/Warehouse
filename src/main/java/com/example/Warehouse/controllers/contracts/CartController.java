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

import java.security.Principal;

public interface CartController extends BaseController {
    @GetMapping("/user")
    String userCartPage(
        Principal principal,
        Model model);

    @GetMapping("/admin")
    String adminCartPage(
        Principal principal,
        Model model);

    @PostMapping("/user/add-product")
    String addProductToUserCart(
        Principal principal,
        @Valid @ModelAttribute("add") AddProductToUserCartForm add,
        BindingResult bindingResult,
        Model model);

    @PostMapping("/admin/add-product")
    String addProductToAdminCart(
        Principal principal,
        @Valid @ModelAttribute("add") AddProductToAdminCartForm add,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/user/delete-product/{productId}")
    String deleteProductFromUserCart(
        Principal principal,
        @PathVariable("productId") String productId,
        Model model);

    @GetMapping("/admin/delete-product/{productId}")
    String deleteProductFromAdminCart(
        Principal principal,
        @PathVariable("productId") String productId,
        Model model);
}
