package com.example.Warehouse.config;

import com.example.Warehouse.domain.models.OrderItem;
import com.example.Warehouse.domain.models.Purchase;
import com.example.Warehouse.dto.auth.RegisterUserDto;
import com.example.Warehouse.dto.category.CategoryAddDto;
import com.example.Warehouse.dto.category.CategoryDto;
import com.example.Warehouse.dto.category.CategorySearchDto;
import com.example.Warehouse.dto.order.OrderDto;
import com.example.Warehouse.dto.order.OrderItemDto;
import com.example.Warehouse.dto.product.ProductSearchByWarehouseDto;
import com.example.Warehouse.dto.product.ProductSearchDto;
import com.example.Warehouse.dto.purchase.PurchaseDto;
import com.example.Warehouse.dto.warehouse.WarehouseSearchDto;
import com.example.WarehouseContracts.dto.forms.category.CategorySearchForm;
import com.example.WarehouseContracts.dto.forms.product.ProductSearchForm;
import com.example.WarehouseContracts.dto.forms.product.ProductWarehouseSearchForm;
import com.example.WarehouseContracts.dto.forms.warehouse.WarehousesSearchForm;
import org.modelmapper.ModelMapper;
import com.example.Warehouse.domain.models.Order;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import com.example.Warehouse.domain.models.Category;
import org.springframework.context.annotation.Configuration;
import com.example.WarehouseContracts.dto.forms.auth.RegisterForm;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.registerModule(new RecordModule());

        modelMapper.typeMap(RegisterForm.class, RegisterUserDto.class).setProvider(ctx -> {
            RegisterForm source = (RegisterForm) ctx.getSource();
            return new RegisterUserDto(
                source.getEmail(),
                source.isRole(),
                source.getUserName(),
                source.getPassword(),
                source.getConfirmPassword()
            );
        });
        modelMapper.typeMap(CategoryAddDto.class, Category.class).setProvider(ctx -> {
            CategoryAddDto source = (CategoryAddDto) ctx.getSource();
            return new Category(
                source.name(),
                1 - (float) source.discount() / 100,
                false
            );
        });

        modelMapper.typeMap(Category.class, CategoryDto.class).setProvider(ctx -> {
            Category source = (Category) ctx.getSource();
            return new CategoryDto(
                source.getId(),
                source.getName(),
                Math.round((1 - source.getDiscount()) * 100),
                source.getIsDeleted()
            );
        });

        modelMapper.typeMap(CategorySearchForm.class, CategorySearchDto.class).setProvider(ctx -> {
            CategorySearchForm source = (CategorySearchForm) ctx.getSource();
            return new CategorySearchDto(
                source.pages().page(),
                source.pages().size(),
                source.pages().substring(),
                source.returnDeleted()
            );
        });

        modelMapper.typeMap(Order.class, OrderDto.class).setProvider(ctx -> {
            Order source = (Order) ctx.getSource();
            return new OrderDto(
                source.getNumber(),
                source.getTotalAmount(),
                source.getDate()
            );
        });

        modelMapper.typeMap(OrderItem.class, OrderItemDto.class).setProvider(ctx -> {
            OrderItem source = (OrderItem) ctx.getSource();
            return new OrderItemDto(
                source.getProduct().getId(),
                source.getQuantity()
            );
        });

        modelMapper.typeMap(Purchase.class, PurchaseDto.class).setProvider(ctx -> {
            Purchase source = (Purchase) ctx.getSource();
            return new PurchaseDto(
                source.getNumber(),
                source.getStatus().name(),
                source.getCashback(),
                source.getTotalPrice(),
                source.getDate()
            );
        });

        modelMapper.typeMap(ProductSearchForm.class, ProductSearchDto.class).setProvider(ctx -> {
            ProductSearchForm source = (ProductSearchForm) ctx.getSource();
            return new ProductSearchDto(
                source.pages().page(),
                source.pages().size(),
                source.category(),
                source.pages().substring(),
                source.priceSort(),
                source.returnDeleted()
            );
        });

        modelMapper.typeMap(ProductWarehouseSearchForm.class, ProductSearchByWarehouseDto.class).setProvider(ctx -> {
            ProductWarehouseSearchForm source = (ProductWarehouseSearchForm) ctx.getSource();
            return new ProductSearchByWarehouseDto(
                source.pages().page(),
                source.pages().size(),
                source.category(),
                source.pages().substring(),
                source.warehouseId(),
                source.returnDeleted()
            );
        });

        modelMapper.typeMap(WarehousesSearchForm.class, WarehouseSearchDto.class).setProvider(ctx -> {
            WarehousesSearchForm source = (WarehousesSearchForm) ctx.getSource();
            return new WarehouseSearchDto(
                source.pages().page(),
                source.pages().size(),
                source.pages().substring(),
                source.returnDeleted()
            );
        });

        return modelMapper;
    }
}
