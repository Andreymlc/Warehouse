package com.example.Warehouse.config;

import com.example.Warehouse.domain.models.Product;
import org.modelmapper.ModelMapper;
import com.example.WarehouseContracts.dto.*;
import com.example.Warehouse.domain.models.Order;
import org.springframework.context.annotation.Bean;
import com.example.Warehouse.domain.models.Category;
import com.example.Warehouse.domain.models.Warehouse;
import org.springframework.context.annotation.Configuration;
import com.example.WarehouseContracts.dto.forms.auth.LoginForm;
import com.example.WarehouseContracts.dto.forms.auth.RegisterForm;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(RegisterForm.class, RegisterUserDto.class).setProvider(ctx -> {
            RegisterForm source = (RegisterForm) ctx.getSource();
            return new RegisterUserDto(
                    source.email(),
                    source.role(),
                    source.userName(),
                    source.password(),
                    source.confirmPassword()
            );
        });

        modelMapper.typeMap(LoginForm.class, LoginUserDto.class).setProvider(ctx -> {
            LoginForm source = (LoginForm) ctx.getSource();
            return new LoginUserDto(
                    source.userName(),
                    source.password()
            );
        });

        modelMapper.typeMap(WarehouseAddDto.class, Warehouse.class).setProvider(ctx -> {
            WarehouseAddDto source = (WarehouseAddDto) ctx.getSource();
            return new Warehouse(
                    source.name(),
                    source.location()
            );
        });

        modelMapper.typeMap(Warehouse.class, WarehouseDto.class).setProvider(ctx -> {
            Warehouse source = (Warehouse) ctx.getSource();
            return new WarehouseDto(
                source.getId(),
                source.getName(),
                source.getLocation()
            );
        });

        modelMapper.typeMap(CategoryAddDto.class, Category.class).setProvider(ctx -> {
            CategoryAddDto source = (CategoryAddDto) ctx.getSource();
            return new Category(
                    source.name(),
                1 - (float) source.discount() / 100
            );
        });

        modelMapper.typeMap(Category.class, CategoryDto.class).setProvider(ctx -> {
            Category source = (Category) ctx.getSource();
            return new CategoryDto(
                source.getId(),
                source.getName(),
                Math.round(((1 - source.getDiscount()) * 100))
            );
        });

        modelMapper.typeMap(Order.class, OrderDto.class).setProvider(ctx -> {
            Order source = (Order) ctx.getSource();
            return new OrderDto(
                source.getId(),
                source.getStatus(),
                source.getOrderDate(),
                source.getTotalAmount()
            );
        });

        return modelMapper;
    }
}
