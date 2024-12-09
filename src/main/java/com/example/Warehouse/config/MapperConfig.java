package com.example.Warehouse.config;

import com.example.Warehouse.dto.*;
import org.modelmapper.ModelMapper;
import com.example.Warehouse.domain.models.Order;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import com.example.Warehouse.domain.models.Category;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Configuration;
import com.example.WarehouseContracts.dto.forms.auth.LoginForm;
import com.example.WarehouseContracts.dto.forms.auth.RegisterForm;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.registerModule(new RecordModule());

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
                Math.round((1 - source.getDiscount()) * 100)
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
