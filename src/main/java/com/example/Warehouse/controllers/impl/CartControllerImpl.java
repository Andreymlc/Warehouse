package com.example.Warehouse.controllers.impl;

import com.example.Warehouse.controllers.contracts.CartController;
import com.example.Warehouse.models.forms.cart.AddProductToAdminCartForm;
import com.example.Warehouse.models.forms.cart.AddProductToUserCartForm;
import com.example.Warehouse.models.forms.order.OrderCreateForm;
import com.example.Warehouse.models.forms.purchase.PurchaseCreateForm;
import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import com.example.Warehouse.models.viewmodels.cart.CartAdminViewModel;
import com.example.Warehouse.models.viewmodels.cart.CartUserViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductInCartViewModel;
import com.example.Warehouse.services.contracts.CartService;
import com.example.Warehouse.services.contracts.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartControllerImpl implements CartController {
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final UserService userService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

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
        LOG.info("User {} requests cart", principal.getName());
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
        if (!model.containsAttribute("purchaseCreate")) model.addAttribute("purchaseCreate", new PurchaseCreateForm(0));

        LOG.info("Return 'cart' for User {}", principal.getName());

        return "cart";
    }

    @Override
    @GetMapping("/admin")
    public String adminCartPage(
        Principal principal,
        Model model
    ) {
        LOG.info("Admin {} requests cart", principal.getName());
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
        if (!model.containsAttribute("orderCreate")) model.addAttribute("orderCreate", new OrderCreateForm(null));

        LOG.info("Return 'cart' for Admin {}", principal.getName());

        return "cart";
    }

    @Override
    @PostMapping("/user/add-product")
    public String addProductToUserCart(
        Principal principal,
        @Valid @ModelAttribute("add") AddProductToUserCartForm add,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            LOG.error(bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("add", add);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.add",
                bindingResult
            );

            return "redirect:/home/user?returnDeleted=false";
        }

        LOG.info("Add product '{}' to User cart", add.productId());
        cartService.addProductToCart(principal.getName(), add.productId());

        LOG.info("Successful add product '{}' to User cart", add.productId());

        return "redirect:/home/user?returnDeleted=false";
    }

    @Override
    @PostMapping("/admin/add-product")
    public String addProductToAdminCart(
        Principal principal,
        @Valid @ModelAttribute("add") AddProductToAdminCartForm add,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            LOG.error(bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("add", add);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.add",
                bindingResult
            );

            return "redirect:/home/user?returnDeleted=false";
        }

        LOG.info("Add product '{}' to Admin cart", add.productId());
        cartService.addProductToCart(principal.getName(), add.productId());

        LOG.info("Successful add product '{}' to Admin cart", add.productId());

        return "redirect:/home/admin?returnDeleted=false";
    }

    @Override
    @GetMapping("/user/delete-product/{productId}")
    public String deleteProductFromUserCart(
        Principal principal,
        @PathVariable("productId") String productId,
        Model model
    ) {
        LOG.info("Delete product '{}' from User cart", productId);
        cartService.deleteProductFromCart(principal.getName(), productId);

        LOG.info("Successful delete product '{}' from User cart", productId);
        return "redirect:/cart/user";
    }

    @Override
    @GetMapping("/admin/delete-product/{productId}")
    public String deleteProductFromAdminCart(
        Principal principal,
        @PathVariable("productId") String productId,
        Model model
    ) {
        LOG.info("Delete product '{}' from Admin cart", productId);
        cartService.deleteProductFromCart(principal.getName(), productId);

        LOG.info("Successful delete product '{}' from Admin cart", productId);

        return "redirect:/cart/admin";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
