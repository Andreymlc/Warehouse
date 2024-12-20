package com.example.Warehouse.controllers.impl;

import com.example.Warehouse.services.contracts.CartService;
import com.example.Warehouse.services.contracts.UserService;
import com.example.Warehouse.controllers.contracts.CartController;
import com.example.Warehouse.models.forms.cart.AddProductToAdminCartForm;
import com.example.Warehouse.models.forms.cart.AddProductToUserCartForm;
import com.example.Warehouse.models.forms.order.OrderCreateForm;
import com.example.Warehouse.models.forms.purchase.PurchaseCreateForm;
import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import com.example.Warehouse.models.viewmodels.cart.CartAdminViewModel;
import com.example.Warehouse.models.viewmodels.cart.CartUserViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductInCartViewModel;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartControllerImpl implements CartController {
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final UserService userService;

    public CartControllerImpl(
        ModelMapper modelMapper,
        CartService cartService,
        UserService userService
    ) {
        this.modelMapper = modelMapper;
        this.cartService = cartService;
        this.userService = userService;
    }

    @Override
    @GetMapping("/user")
    public String userCartPage(
        Principal principal,
        Model model
    ) {
        var cart = cartService.findCart(principal.getName());

        var productsViewModel = cart
            .products()
            .stream()
            .map(productCartDto -> modelMapper.map(productCartDto, ProductInCartViewModel.class))
            .toList();

        var pointsCount = userService.getPointsCount(principal.getName());

        var viewModel = new CartUserViewModel(
            cart.totalPrice(),
            pointsCount,
            createBaseViewModel(0, productsViewModel.size()),
            productsViewModel
        );

        model.addAttribute("model", viewModel);
        model.addAttribute("orderCreate", new PurchaseCreateForm(0));

        return "cart";
    }

    @Override
    @GetMapping("/admin")
    public String adminCartPage(
        Principal principal,
        Model model
    ) {
        var cart = cartService.findCart(principal.getName());

        var productsViewModel = cart
            .products()
            .stream()
            .map(productCartDto -> modelMapper.map(productCartDto, ProductInCartViewModel.class))
            .toList();

        var viewModel = new CartAdminViewModel(
            cart.totalPrice(),
            createBaseViewModel(0, productsViewModel.size()),
            productsViewModel
        );

        model.addAttribute("model", viewModel);
        model.addAttribute("orderCreate", new OrderCreateForm(null));

        return "cart";
    }

    @Override
    @PostMapping("/user/add-product")
    public String addProductToUserCart(
        Principal principal,
        @Valid @ModelAttribute("add") AddProductToUserCartForm add,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.addProductToCart(principal.getName(), add.productId());

        return "redirect:/home/user?returnDeleted=false";
    }

    @Override
    @PostMapping("/admin/add-product")
    public String addProductToAdminCart(
        Principal principal,
        @Valid @ModelAttribute("add") AddProductToAdminCartForm add,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.addProductToCart(principal.getName(), add.productId());

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @GetMapping("/user/delete-product/{productId}")
    public String deleteProductFromUserCart(
        Principal principal,
        @PathVariable("productId") String productId,
        Model model
    ) {
        cartService.deleteProductFromCart(principal.getName(), productId);

        return "redirect:/cart/user";
    }

    @Override
    @GetMapping("/admin/delete-product/{productId}")
    public String deleteProductFromAdminCart(
        Principal principal,
        @PathVariable("productId") String productId,
        Model model
    ) {
        cartService.deleteProductFromCart(principal.getName(), productId);

        return "redirect:/cart/admin";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
