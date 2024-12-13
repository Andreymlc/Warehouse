package com.example.Warehouse.controllers;

import com.example.Warehouse.dto.PurchaseDto;
import com.example.Warehouse.services.OrderService;
import com.example.Warehouse.services.PurchaseService;
import com.example.Warehouse.utils.UrlUtil;
import com.example.WarehouseContracts.controllers.OrderController;
import com.example.WarehouseContracts.dto.forms.purchase.PurchaseChangeStatusForm;
import com.example.WarehouseContracts.dto.forms.order.OrderCreateForm;
import com.example.WarehouseContracts.dto.forms.order.OrdersSearchForm;
import com.example.WarehouseContracts.dto.forms.purchase.PurchaseCreateForm;
import com.example.WarehouseContracts.dto.forms.purchase.PurchasesSearchForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.order.OrderViewModel;
import com.example.WarehouseContracts.dto.viewmodels.order.OrdersPageViewModel;
import com.example.WarehouseContracts.dto.viewmodels.purchase.PurchasePageViewModel;
import com.example.WarehouseContracts.dto.viewmodels.purchase.PurchaseViewModel;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    private final OrderService orderService;
    private final PurchaseService purchaseService;

    public OrderControllerImpl(
        OrderService orderService,
        PurchaseService purchaseService
    ) {
        this.orderService = orderService;
        this.purchaseService = purchaseService;
    }

    @Override
    @GetMapping("/admin")
    public String getOrders(
        @Valid @ModelAttribute("form") OrdersSearchForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "orders";
        }

        var ordersPage = orderService.findOrders(
            form.base().id(),
            form.pages().page(),
            form.pages().size()
        );

        var ordersViewModel = ordersPage
            .stream()
            .map(op -> new OrderViewModel(
                    op.number(),
                    op.totalPrice(),
                    op.date()
                )
            ).toList();

        var viewModel = new OrdersPageViewModel(
            createBaseViewModel(ordersPage.getTotalPages(), 0),
            ordersViewModel
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);

        return "orders";
    }

    @GetMapping("/user")
    public String getPurchases(
        @Valid @ModelAttribute("form") PurchasesSearchForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "orders";
        }

        Page<PurchaseDto> purchasesPage = purchaseService.findPurchases(
            form.base().id(),
            form.pages().page(),
            form.pages().size()
        );

        var purchasesViewModel = purchasesPage
            .stream()
            .map(op -> new PurchaseViewModel(
                    op.number(),
                    op.status(),
                    op.cashback(),
                    op.totalPrice(),
                    op.date()
                )
            ).toList();

        var viewModel = new PurchasePageViewModel(
            createBaseViewModel(purchasesPage.getTotalPages(), 0),
            purchasesViewModel
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);

        return "orders";
    }

    @GetMapping("/manage-purchase")
    public String managePurchasePage(
        @Valid @ModelAttribute("form") PurchasesSearchForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "orders";
        }

        Page<PurchaseDto> purchasesPage = purchaseService.findAllPurchases(
            form.pages().page(),
            form.pages().size()
        );

        var purchasesViewModel = purchasesPage
            .stream()
            .map(op -> new PurchaseViewModel(
                    op.number(),
                    op.status(),
                    op.cashback(),
                    op.totalPrice(),
                    op.date()
                )
            ).toList();

        var viewModel = new PurchasePageViewModel(
            createBaseViewModel(purchasesPage.getTotalPages(), 0),
            purchasesViewModel
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);

        return "purchase-manage";
    }

    @Override
    @GetMapping("/check")
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

        return "redirect:" + UrlUtil.managePurchaseUrl(form.base());
    }

    @GetMapping("/canceled")
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

        return "redirect:" + UrlUtil.managePurchaseUrl(form.base());
    }

    @Override
    @PostMapping("/admin/create")
    public String createAdminOrder(
        @Valid @ModelAttribute("form") OrderCreateForm form,
        BindingResult bindingResult,
        Model model
    ) {

        orderService.addOrder(form.base().id(), form.warehouseId());

        return "redirect:" + UrlUtil.orderUrl(form.base());
    }

    @Override
    @PostMapping("/user/create")
    public String createUserPurchase(
        @Valid @ModelAttribute("form") PurchaseCreateForm form,
        BindingResult bindingResult,
        Model model
    ) {
        purchaseService.addPurchase(form.base().id(), form.pointsSpent());

        return "redirect:" + UrlUtil.purchaseUrl(form.base());
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
