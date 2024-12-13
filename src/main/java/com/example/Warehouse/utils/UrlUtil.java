package com.example.Warehouse.utils;

import com.example.WarehouseContracts.dto.forms.base.BaseForm;

public class UrlUtil {

    public static String homeUrl(BaseForm form) {
        return "/home/" + form.role().toLowerCase() +
            "?priceSort=true" +
            "&base.id=" + form.id() +
            "&base.role=" + form.role() +
            "&base.userName=" + form.userName();
    }

    public static String cartUrl(BaseForm form) {
        return "/cart" +
            "?base.id=" + form.id() +
            "&base.role=" + form.role() +
            "&base.userName=" + form.userName();
    }

    public static String orderUrl(BaseForm form) {
        return "/orders/admin" +
            "?base.id=" + form.id() +
            "&base.role=" + form.role() +
            "&base.userName=" + form.userName();
    }

    public static String purchaseUrl(BaseForm form) {
        return "/orders/user" +
            "?base.id=" + form.id() +
            "&base.role=" + form.role() +
            "&base.userName=" + form.userName();
    }

    public static String managePurchaseUrl(BaseForm form) {
        return "/orders/manage-purchase" +
            "?base.id=" + form.id() +
            "&base.role=" + form.role() +
            "&base.userName=" + form.userName();
    }

    public static String warehousesUrl(BaseForm form) {
        return "/home/admin/warehouses?" +
            "base.id=" + form.id() +
            "&base.role=" + form.role() +
            "&base.userName=" + form.userName();
    }

    public static String categoriesUrl(BaseForm form) {
        return "/home/admin/categories?" +
            "base.id=" + form.id() +
            "&base.role=" + form.role() +
            "&base.userName=" + form.userName();
    }

    public static String warehouseManageUrl(BaseForm form, String warehouseId) {
        return "/warehouses/" + warehouseId + "/manage?" +
            "warehouseId=" + warehouseId +
            "&base.id=" + form.id() +
            "&base.role=" + form.role() +
            "&base.userName=" + form.userName();
    }
}
