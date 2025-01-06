package com.example.Warehouse.controllers.contracts;

import com.example.Warehouse.models.forms.auth.RegisterForm;
import jakarta.validation.Valid;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/auth")
public interface AuthController {

    @GetMapping("/register")
    String registerForm();

    @PostMapping("/register")
    String register(
        @Valid @ModelAttribute("form") RegisterForm form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes);

    @GetMapping("/login")
    String loginForm();

    @PostMapping("/login-error")
    String onFailedLogin(
        @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
        RedirectAttributes redirectAttributes);
}
