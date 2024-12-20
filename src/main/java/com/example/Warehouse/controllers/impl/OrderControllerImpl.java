package com.example.Warehouse.controllers.impl;

import com.example.Warehouse.controllers.contracts.OrderController;
import com.example.Warehouse.models.forms.order.OrderCreateForm;
import com.example.Warehouse.models.forms.order.OrdersSearchForm;
import com.example.Warehouse.models.forms.purchase.PurchaseChangeStatusForm;
import com.example.Warehouse.models.forms.purchase.PurchaseCreateForm;
import com.example.Warehouse.models.forms.purchase.PurchasesSearchForm;
import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;
import com.example.Warehouse.models.viewmodels.order.OrderViewModel;
import com.example.Warehouse.models.viewmodels.order.OrdersPageViewModel;
import com.example.Warehouse.models.viewmodels.purchase.PurchasePageViewModel;
import com.example.Warehouse.models.viewmodels.purchase.PurchaseViewModel;
import com.example.Warehouse.services.contracts.OrderService;
import com.example.Warehouse.services.contracts.PurchaseService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final PurchaseService purchaseService;

    public OrderControllerImpl(
        ModelMapper modelMapper,
        OrderService orderService,
        PurchaseService purchaseService
    ) {
        this.modelMapper = modelMapper;
        this.orderService = orderService;
        this.purchaseService = purchaseService;
    }

    @Override
    @GetMapping("/admin")
    public String getOrders(
        Authentication authentication,
        @Valid @ModelAttribute("form") OrdersSearchForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "orders";
        }

        var ordersPage = orderService.findOrders(
            authentication.getName(),
            form.pages().page(),
            form.pages().size()
        );

        var ordersViewModel = ordersPage
            .stream()
            .map(orderDto -> modelMapper.map(orderDto, OrderViewModel.class))
            .toList();

        var viewModel = new OrdersPageViewModel(
            createBaseViewModel(ordersPage.getTotalPages(), 0),
            ordersViewModel
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);

        return "orders";
    }

    @Override
    @GetMapping("/user")
    public String getPurchases(
        Authentication authentication,
        @Valid @ModelAttribute("form") PurchasesSearchForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "orders";
        }

        var purchasesPage = purchaseService.findPurchases(
            authentication.getName(),
            form.pages().page(),
            form.pages().size()
        );

        var purchasesViewModel = purchasesPage
            .stream()
            .map(purchaseDto -> modelMapper.map(purchaseDto, PurchaseViewModel.class))
            .toList();

        var viewModel = new PurchasePageViewModel(
            createBaseViewModel(purchasesPage.getTotalPages(), 0),
            purchasesViewModel
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);

        return "orders";
    }

    @Override
    @GetMapping("/admin/manage-purchase")
    public String managePurchasePage(
        @Valid @ModelAttribute("form") PurchasesSearchForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "orders";
        }

        var purchasesPage = purchaseService.findAllPurchases(
            form.pages().page(),
            form.pages().size()
        );

        var purchasesViewModel = purchasesPage
            .stream()
            .map(purchaseDto -> modelMapper.map(purchaseDto, PurchaseViewModel.class))
            .toList();

        var viewModel = new PurchasePageViewModel(
            createBaseViewModel(purchasesPage.getTotalPages(), 0),
            purchasesViewModel
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);

        return "purchase-manage";
    }

    @Override
    @GetMapping("/admin/check")
    public String changeStatus(
        @Valid @ModelAttribute("form") PurchaseChangeStatusForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "purchase-manage";
        }

        purchaseService.check(form.purchaseNumber());

        model.addAttribute("form", form);

        return "redirect:/orders/manage-purchase";
    }

    @Override
    @GetMapping("/admin/canceled")
    public String setCanceledStatus(
        @Valid @ModelAttribute("form") PurchaseChangeStatusForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "purchase-manage";
        }

        purchaseService.setCanceled(form.purchaseNumber());

        model.addAttribute("form", form);

        return "redirect:/orders/manage-purchase";
    }

    @Override
    @PostMapping("/admin/create")
    public String createAdminOrder(
        Authentication authentication,
        @Valid @ModelAttribute("form") OrderCreateForm form,
        BindingResult bindingResult,
        Model model
    ) {
        orderService.addOrder(authentication.getName(), form.warehouseId());

        return "redirect:/orders/admin";
    }

    @Override
    @PostMapping("/user/create")
    public String createUserPurchase(
        Authentication authentication,
        @Valid @ModelAttribute("form") PurchaseCreateForm form,
        BindingResult bindingResult,
        Model model
    ) {
        purchaseService.addPurchase(authentication.getName(), form.pointsSpent());

        return "redirect:/orders/user";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
