package com.example.Warehouse.controllers;

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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartControllerImpl implements CartController {
    private final CartService cartService;
    private final UserService userService;

    public CartControllerImpl(
        CartService cartService,
        UserService userService
    ) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @Override
    @GetMapping("/user")
    public String userCartPage(
        Authentication authentication,
        Model model
    ) {
        var cart = cartService.findCart(authentication.getName());

        var productsViewModel = cart
            .products()
            .stream()
            .map(p -> new ProductInCartViewModel(
                    p.id(),
                    p.name(),
                    p.category(),
                    p.quantity(),
                    p.totalPrice()
                )
            )
            .toList();

        var pointsCount = userService.getPointsCount(authentication.getName());

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
        Authentication authentication,
        Model model
    ) {
        var cart = cartService.findCart(authentication.getName());

        var productsViewModel = cart
            .products()
            .stream()
            .map(p -> new ProductInCartViewModel(
                    p.id(),
                    p.name(),
                    p.category(),
                    p.quantity(),
                    p.totalPrice()
                )
            )
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
        Authentication authentication,
        @Valid @ModelAttribute("add") AddProductToUserCartForm add,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.addProductToCart(authentication.getName(), add.productId());

        return "redirect:/home/user?returnDeleted=false";
    }

    @Override
    @PostMapping("/admin/add-product")
    public String addProductToAdminCart(
        Authentication authentication,
        @Valid @ModelAttribute("add") AddProductToAdminCartForm add,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.addProductToCart(authentication.getName(), add.productId());

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @GetMapping("/user/delete-product/{productId}")
    public String deleteProductFromUserCart(
        Authentication authentication,
        @PathVariable("productId") String productId,
        Model model
    ) {
        cartService.deleteProductFromCart(authentication.getName(), productId);

        return "redirect:/cart/user";
    }

    @Override
    @GetMapping("/admin/delete-product/{productId}")
    public String deleteProductFromAdminCart(
        Authentication authentication,
        @PathVariable("productId") String productId,
        Model model
    ) {
        cartService.deleteProductFromCart(authentication.getName(), productId);

        return "redirect:/cart/admin";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
