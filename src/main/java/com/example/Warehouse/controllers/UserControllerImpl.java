package com.example.Warehouse.controllers;

import com.example.Warehouse.dto.LoginUserDto;
import com.example.Warehouse.dto.RegisterUserDto;
import com.example.Warehouse.utils.UrlUtil;
import com.example.Warehouse.services.UserService;
import com.example.WarehouseContracts.controllers.UserController;
import com.example.WarehouseContracts.dto.forms.auth.LoginForm;
import com.example.WarehouseContracts.dto.forms.auth.RegisterForm;
import com.example.WarehouseContracts.dto.forms.base.BaseForm;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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
        model.addAttribute(
            "form",
            new RegisterForm("", false, "", "", "")
        );

        return "register";
    }

    @Override
    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute("form") RegisterForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "register";
        }

        var result = userService.register(modelMapper.map(form, RegisterUserDto.class));

        return "redirect:" +
            UrlUtil.homeUrl(new BaseForm(result.id(), result.role().name(), form.userName()));
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
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "login";
        }

        var result = userService.login(modelMapper.map(form, LoginUserDto.class));

        return "redirect:" +
            UrlUtil.homeUrl(new BaseForm(result.id(), result.role().name(), form.userName()));
    }

    @Override
    public String logout(LoginForm form, BindingResult bindingResult, Model model) {
        return "";
    }
}
