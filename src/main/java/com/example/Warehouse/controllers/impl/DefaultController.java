package com.example.Warehouse.controllers.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/catalog/admin/products?returnDeleted=false";
        }
        return "redirect:/catalog?returnDeleted=false";
    }
}
