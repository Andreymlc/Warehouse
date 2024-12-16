package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.forms.order.OrderCreateForm;
import com.example.Warehouse.models.forms.order.OrdersSearchForm;
import com.example.Warehouse.models.forms.purchase.PurchaseChangeStatusForm;
import com.example.Warehouse.models.forms.purchase.PurchaseCreateForm;
import com.example.Warehouse.models.forms.purchase.PurchasesSearchForm;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/orders")
public interface OrderController extends BaseController {

    @GetMapping("/admin")
    String getOrders(
        Authentication authentication,
        @Valid @ModelAttribute("form") OrdersSearchForm form,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/user")
    String getPurchases(
        Authentication authentication,
        @Valid @ModelAttribute("form") PurchasesSearchForm form,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/manage-purchase")
    String managePurchasePage(
        @Valid @ModelAttribute("form") PurchasesSearchForm form,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/check")
    String changeStatus(
        @Valid @ModelAttribute("form") PurchaseChangeStatusForm form,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/canceled")
    String setCanceledStatus(
        @Valid @ModelAttribute("form") PurchaseChangeStatusForm form,
        BindingResult bindingResult,
        Model model);

    @PostMapping("/admin/create")
    String createAdminOrder(
        Authentication authentication,
        @Valid @ModelAttribute("form") OrderCreateForm form,
        BindingResult bindingResult,
        Model model);

    @PostMapping("/user/create")
    String createUserPurchase(
        Authentication authentication,
        @Valid @ModelAttribute("form") PurchaseCreateForm form,
        BindingResult bindingResult,
        Model model);

}
