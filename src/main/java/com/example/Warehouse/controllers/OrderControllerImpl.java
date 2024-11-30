package com.example.Warehouse.controllers;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import com.example.Warehouse.services.OrderService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.WarehouseContracts.controllers.OrderController;
import com.example.WarehouseContracts.dto.forms.order.OrdersSearchForm;
import com.example.WarehouseContracts.dto.forms.order.CreateOrderUserForm;
import com.example.WarehouseContracts.dto.viewmodels.order.OrderViewModel;
import com.example.WarehouseContracts.dto.forms.order.CreateOrderAdminForm;
import com.example.WarehouseContracts.dto.forms.order.ChangeOrderStatusForm;
import com.example.WarehouseContracts.dto.viewmodels.base.BasePagesViewModel;
import com.example.WarehouseContracts.dto.viewmodels.order.OrdersPageViewModel;

@Controller
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {
    private final OrderService orderService;

    public OrderControllerImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    @GetMapping
    public String ordersPage(
        @Valid @ModelAttribute("form")OrdersSearchForm form,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) return "orders";

        var ordersPage = orderService.getOrders(
            form.status(),
            form.dateSort(),
            form.pages().page(),
            form.pages().size()
        );

        var ordersViewModel = ordersPage
            .stream()
            .map(op -> new OrderViewModel(
                op.number(),
                op.status().toString(),
                op.date(),
                op.totalPrice())
            ).toList();

        var viewModel = new OrdersPageViewModel(
            createBaseViewModel(ordersPage.getTotalPages(), 0),
            ordersViewModel
        );

        model.addAttribute("form", form);
        model.addAttribute("model", viewModel);

        return "orders";
    }

    @Override
    @PostMapping("/change-status")
    public String changeStatus(
        @Valid @ModelAttribute("form")ChangeOrderStatusForm form,
        BindingResult bindingResult,
        Model model) {

        return "ordersa";
    }

    @Override
    @PostMapping("/admin/create-order")
    public String createAdminOrder(
        @Valid @ModelAttribute("form")CreateOrderAdminForm form,
        BindingResult bindingResult,
        Model model) {

        return "ordersa";
    }

    @Override
    @PostMapping("/user/create-order")
    public String createUserOrder(
        @Valid @ModelAttribute("form")CreateOrderUserForm form,
        BindingResult bindingResult,
        Model model) {

        return "ordersa";
    }

    @Override
    public BasePagesViewModel createBaseViewModel(Integer totalPages, Integer countItemsInCart) {
        return new BasePagesViewModel(totalPages, countItemsInCart);
    }
}
