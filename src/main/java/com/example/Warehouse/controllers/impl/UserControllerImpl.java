package com.example.Warehouse.controllers.impl;

import com.example.Warehouse.models.dto.auth.RegisterUserDto;
import com.example.Warehouse.services.contracts.UserService;
import com.example.Warehouse.controllers.contracts.UserController;
import com.example.Warehouse.models.forms.auth.RegisterForm;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/users")
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserControllerImpl(
        UserService userService,
        ModelMapper modelMapper
    ) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("form")
    public RegisterForm initForm() {
        return new RegisterForm();
    }

    @Override
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @Override
    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute("form") RegisterForm form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationDto", form);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);

            return "redirect:/users/register";
        }

        userService.register(modelMapper.map(form, RegisterUserDto.class));

        return "redirect:/users/login";
    }

    @Override
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
        @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
        RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        return "redirect:/users/login";
    }
}
