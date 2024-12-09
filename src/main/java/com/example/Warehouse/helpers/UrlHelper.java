package com.example.Warehouse.helpers;

import com.example.WarehouseContracts.dto.forms.base.BaseUserForm;
import com.example.WarehouseContracts.dto.forms.base.BaseAdminForm;

public class UrlHelper {

    public static String userHomeUrl(BaseUserForm form) {
        return "redirect:/home/user?" +
            "base.userName=" + form.userName() +
            "&base.role=" + form.role() +
            "&base.id=" + form.id() +
            "&base.pointsCount=" + form.pointsCount() +
            "&priceSort=true";
    }

    public static String adminWarehousesUrl(BaseAdminForm form) {
        return "/home/admin/warehouses?" +
            "base.userName=" + form.userName() +
            "&base.id=" + form.id() +
            "&base.role=" + form.role();
    }

    public static String adminProductUrl(BaseAdminForm form) {
        return "/home/admin/products?" +
            "base.userName=" + form.userName() +
            "&base.id=" + form.id() +
            "&base.role=" + form.role() +
            "priceSort=true";
    }

    public static String adminCategoriesUrl(BaseAdminForm form) {
        return "/home/admin/categories?" +
            "base.userName=" + form.userName() +
            "&base.id=" + form.id() +
            "&base.role=" + form.role();
    }


}
