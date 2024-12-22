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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final PurchaseService purchaseService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

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
        Principal principal,
        @ModelAttribute("form") OrdersSearchForm form,
        Model model
    ) {

        LOG.info("Admin {} requests orders", principal.getName());
        var ordersPage = orderService.findOrders(
            principal.getName(),
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

        LOG.info("Return 'orders' for Admin {}", principal.getName());

        return "orders";
    }

    @Override
    @GetMapping("/user")
    public String getPurchases(
        Principal principal,
        @ModelAttribute("form") PurchasesSearchForm form,
        Model model
    ) {
        LOG.info("User {} requests purchases", principal.getName());
        var purchasesPage = purchaseService.findPurchases(
            principal.getName(),
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

        LOG.info("Return 'orders' for User {}", principal.getName());

        return "orders";
    }

    @Override
    @GetMapping("/admin/manage-purchase")
    public String managePurchasePage(
        @ModelAttribute("form") PurchasesSearchForm form,
        Model model
    ) {
        LOG.info("Admin requests all user purchase");
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

        LOG.info("Return 'purchase-manage'");

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

        LOG.info("Checking purchase {}", form.purchaseNumber());
        purchaseService.check(form.purchaseNumber());

        model.addAttribute("form", form);

        LOG.info("Successful checking purchase {}", form.purchaseNumber());

        return "redirect:/orders/admin/manage-purchase";
    }

    @Override
    @GetMapping("/admin/cancel-purchase")
    public String setCanceledStatus(
        @Valid @ModelAttribute("form") PurchaseChangeStatusForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "purchase-manage";
        }

        LOG.info("Cancel purchase {}", form.purchaseNumber());
        purchaseService.setCanceled(form.purchaseNumber());

        model.addAttribute("form", form);

        LOG.info("Successful cancel purchase {}", form.purchaseNumber());

        return "redirect:/orders/admin/manage-purchase";
    }

    @Override
    @PostMapping("/admin/create")
    public String createAdminOrder(
        Principal principal,
        @Valid @ModelAttribute("orderCreate") OrderCreateForm orderCreate,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderCreate", orderCreate);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.orderCreate",
                bindingResult
            );

            return "redirect:/cart/admin";
        }

        LOG.info("Admin '{}' create order for warehouse: {}", principal.getName(), orderCreate.warehouseId());
        orderService.addOrder(principal.getName(), orderCreate.warehouseId());

        LOG.info("Successful create order for warehouse: {}", orderCreate.warehouseId());

        return "redirect:/orders/admin";
    }

    @Override
    @PostMapping("/user/create")
    public String createUserPurchase(
        Principal principal,
        @Valid @ModelAttribute("purchaseCreate") PurchaseCreateForm purchaseCreate,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("purchaseCreate", purchaseCreate);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.purchaseCreate",
                bindingResult
            );

            return "redirect:/cart/user";
        }

        LOG.info("User '{}' create purchase", principal.getName());
        purchaseService.addPurchase(principal.getName(), purchaseCreate.pointsSpent());

        LOG.info("Successful create purchase");

        return "redirect:/orders/user";
    }

    @GetMapping("/{number}/purchase-details")
    public String getPurchaseDetails(
        Model model,
        @PathVariable("number") String number
    ) {
        var details = purchaseService.findPurchaseDetails(number);

        model.addAttribute("model", details);

        return "purchase-details";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(int totalPages, int countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
