package com.example.Warehouse.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import com.example.Warehouse.dto.LoginUserDto;
import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.dto.RegisterUserDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.example.Warehouse.services.UserService;
import org.springframework.validation.BindingResult;
import com.example.WarehouseContracts.dto.forms.auth.LoginForm;
import com.example.WarehouseContracts.controllers.UserController;
import com.example.WarehouseContracts.dto.forms.auth.RegisterForm;


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

    @Override
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm("", false,"", "", ""));

        return "register";
    }

    @Override
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("form") RegisterForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "register";
        }

        var result = userService.register(modelMapper.map(form, RegisterUserDto.class));

        if (result.role() == Roles.ADMIN) {
            return "redirect:/home/admin/warehouses?" +
                "priceSort=true" +
                "&base.userName=" + form.userName() +
                "&base.role=" + result.role().name();
        } else {
            return "redirect:/home/user?" +
                "priceSort=true" +
                "&base.id=" + result.id() +
                "&base.userName=" + form.userName() +
                "&base.role=" + result.role().name();
        }
    }

    @Override
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("form", new LoginForm("", ""));

        return "login";
    }

    @Override
    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("form") LoginForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "login";
        }

        var result = userService.login(modelMapper.map(form, LoginUserDto.class));

        if (result.role() == Roles.ADMIN) {
            return "redirect:/home/admin/warehouses?" +
                "priceSort=true&" +
                "base.userName=" + form.userName() +
                "&base.role=" + result.role().name();
        } else {
            return "redirect:/home/user?" +
                "priceSort=true" +
                "&userName=" + form.userName() +
                "&role=" + result.role().name();
        }
    }

    @Override
    public String logout(LoginForm form, BindingResult bindingResult, Model model) {
        return "";
    }
}
