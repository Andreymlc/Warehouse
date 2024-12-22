package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.forms.order.OrderCreateForm;
import com.example.Warehouse.models.forms.order.OrdersSearchForm;
import com.example.Warehouse.models.forms.purchase.PurchaseChangeStatusForm;
import com.example.Warehouse.models.forms.purchase.PurchaseCreateForm;
import com.example.Warehouse.models.forms.purchase.PurchasesSearchForm;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequestMapping("/orders")
public interface OrderController extends BaseController {

    @GetMapping("/admin")
    String getOrders(
        Principal principal,
        @ModelAttribute("form") OrdersSearchForm form,
        Model model);

    @GetMapping("/user")
    String getPurchases(
        Principal principal,
        @ModelAttribute("form") PurchasesSearchForm form,
        Model model);

    @GetMapping("/admin/manage-purchase")
    String managePurchasePage(
        @ModelAttribute("form") PurchasesSearchForm form,
        Model model);

    @GetMapping("/admin/check")
    String changeStatus(
        @Valid @ModelAttribute("form") PurchaseChangeStatusForm form,
        BindingResult bindingResult,
        Model model);

    @GetMapping("/admin/canceled")
    String setCanceledStatus(
        @Valid @ModelAttribute("form") PurchaseChangeStatusForm form,
        BindingResult bindingResult,
        Model model);

    @PostMapping("/admin/create")
    String createAdminOrder(
        Principal principal,
        @Valid @ModelAttribute("form") OrderCreateForm form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes);

    @PostMapping("/user/create")
    String createUserPurchase(
        Principal principal,
        @Valid @ModelAttribute("form") PurchaseCreateForm form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes);

}
