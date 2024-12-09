package com.example.Warehouse.controllers;

import com.example.Warehouse.dto.CartDto;
import com.example.WarehouseContracts.dto.forms.base.BaseAdminForm;
import com.example.WarehouseContracts.dto.forms.base.BaseUserForm;
import com.example.WarehouseContracts.dto.forms.cart.*;
import com.example.WarehouseContracts.dto.forms.order.CreateOrderUserForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.example.Warehouse.services.CartService;
import org.springframework.validation.BindingResult;
import com.example.WarehouseContracts.dto.viewmodels.cart.CartUserViewModel;
import com.example.WarehouseContracts.dto.viewmodels.cart.CartAdminViewModel;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductInCartViewModel;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartControllerImpl {
    private final CartService cartService;

    public CartControllerImpl(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user")
    public String userCartPage(
        @Valid @ModelAttribute("form") CartUserForm form,
        BindingResult bindingResult,
        Model model
    ) {
        var cart = cartService.findCart(form.base().id());

        var productsViewModel = createProductInCartViewModel(cart);

        var viewModel = new CartUserViewModel(
            cart.totalPrice(),
            form.base().pointsCount(),
            createBaseViewModel(0, productsViewModel.size()),
            productsViewModel
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("delete", new DeleteProductFromUserCartFrom(null, new BaseUserForm(null, null, null, 0)));
        model.addAttribute("orderCreate", new CreateOrderUserForm(new BaseUserForm(form.base().id(), form.base().role(), form.base().userName(), 0), 0));

        return "cart";
    }

    @GetMapping("/admin")
    public String adminCartPage(
        @Valid @ModelAttribute("form") CartAdminForm form,
        BindingResult bindingResult,
        Model model
    ) {
        var cart = cartService.findCart(form.base().id());

        var productsViewModel = createProductInCartViewModel(cart);

        var viewModel = new CartAdminViewModel(
            cart.totalPrice(),
            createBaseViewModel(0, productsViewModel.size()),
            productsViewModel
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("delete", new DeleteProductFromAdminCartFrom(null, new BaseAdminForm(null, null, null)));

        return "cart";
    }

    @PostMapping("/user/add-product")
    public String addProductToUserCart(
        @Valid @ModelAttribute("add") AddProductToUserCartForm add,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.addProductToCart(add.base().id(), add.productId());

        return "redirect:/home/user?" +
            "base.userName=" + add.base().userName() +
            "&base.role=" + add.base().role() +
            "&base.id=" + add.base().id() +
            "&priceSort=true";
    }

    @PostMapping("/admin/add-product")
    public String addProductToAdminCart(
        @Valid @ModelAttribute("add") AddProductToAdminCartForm add,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.addProductToCart(add.base().id(), add.productId());

        return "redirect:/home/admin/products?" +
            "base.userName=" + add.base().userName() +
            "&base.role=" + add.base().role() +
            "&base.id=" + add.base().id() +
            "&priceSort=true";
    }

    @GetMapping("/user/delete-product")
    public String deleteProductFromUserCart(
        @Valid @ModelAttribute("delete") DeleteProductFromUserCartFrom delete,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.deleteProductFromCart(delete.base().id(), delete.productId());

        return "redirect:/cart/user?" +
            "base.id=" + delete.base().id() +
            "&base.userName=" + delete.base().userName() +
            "&base.role=" + delete.base().role();
    }

    @GetMapping("/admin/delete-product")
    public String deleteProductFromAdminCart(
        @Valid @ModelAttribute("delete") DeleteProductFromAdminCartFrom delete,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.deleteProductFromCart(delete.base().id(), delete.productId());

        return "redirect:/cart/admin?" +
            "base.id=" + delete.base().id() +
            "&base.userName=" + delete.base().userName() +
            "&base.role=" + delete.base().role();
    }

    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }

    private List<ProductInCartViewModel> createProductInCartViewModel(CartDto cartDto) {
        return cartDto
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
    }
}
