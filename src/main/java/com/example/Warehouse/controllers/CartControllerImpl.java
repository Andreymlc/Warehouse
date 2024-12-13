package com.example.Warehouse.controllers;

import com.example.Warehouse.services.CartService;
import com.example.Warehouse.services.UserService;
import com.example.Warehouse.utils.UrlUtil;
import com.example.WarehouseContracts.dto.forms.cart.AddProductToUserCartForm;
import com.example.WarehouseContracts.dto.forms.cart.CartUserForm;
import com.example.WarehouseContracts.dto.forms.cart.DeleteProductFromUserCartFrom;
import com.example.WarehouseContracts.dto.forms.order.OrderCreateForm;
import com.example.WarehouseContracts.dto.forms.purchase.PurchaseCreateForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.cart.CartAdminViewModel;
import com.example.WarehouseContracts.dto.viewmodels.cart.CartUserViewModel;
import com.example.WarehouseContracts.dto.viewmodels.product.ProductInCartViewModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartControllerImpl {
    private final CartService cartService;
    private final UserService userService;

    public CartControllerImpl(
        CartService cartService,
        UserService userService
    ) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping
    public String cartPage(
        @Valid @ModelAttribute("form") CartUserForm form,
        BindingResult bindingResult,
        Model model
    ) {
        var cart = cartService.findCart(form.base().id());

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

        var viewModel = switch (form.base().role()) {
            case "USER" -> {
                var pointsCount = userService.getPointsCount(form.base().id());

                model.addAttribute("orderCreate", new PurchaseCreateForm(form.base(), 0));

                yield new CartUserViewModel(
                    cart.totalPrice(),
                    pointsCount,
                    createBaseViewModel(0, productsViewModel.size()),
                    productsViewModel
                );
            }

            case "ADMIN" -> {
                model.addAttribute("orderCreate", new OrderCreateForm(form.base(), null));

                yield new CartAdminViewModel(
                    cart.totalPrice(),
                    createBaseViewModel(0, productsViewModel.size()),
                    productsViewModel
                );
            }

            default -> null;
        };

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);
        model.addAttribute("delete", new DeleteProductFromUserCartFrom(null, form.base()));

        return "cart";
    }

    @PostMapping("/add-product")
    public String addProductToUserCart(
        @Valid @ModelAttribute("add") AddProductToUserCartForm add,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.addProductToCart(add.base().id(), add.productId());

        return "redirect:" + UrlUtil.homeUrl(add.base());
    }

    @GetMapping("/delete-product")
    public String deleteProductFromUserCart(
        @Valid @ModelAttribute("delete") DeleteProductFromUserCartFrom delete,
        BindingResult bindingResult,
        Model model
    ) {
        cartService.deleteProductFromCart(delete.base().id(), delete.productId());

        return "redirect:" + UrlUtil.cartUrl(delete.base());
    }

    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
