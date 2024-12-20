package com.example.Warehouse.config;

import com.example.Warehouse.domain.entities.Category;
import com.example.Warehouse.domain.entities.Order;
import com.example.Warehouse.domain.entities.OrderItem;
import com.example.Warehouse.domain.entities.Purchase;
import com.example.Warehouse.models.dto.auth.RegisterUserDto;
import com.example.Warehouse.models.dto.category.CategoryAddDto;
import com.example.Warehouse.models.dto.category.CategoryDto;
import com.example.Warehouse.models.dto.category.CategorySearchDto;
import com.example.Warehouse.models.dto.order.OrderDto;
import com.example.Warehouse.models.dto.order.OrderItemDto;
import com.example.Warehouse.models.dto.product.*;
import com.example.Warehouse.models.dto.purchase.PurchaseDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseDto;
import com.example.Warehouse.models.dto.warehouse.WarehouseSearchDto;
import com.example.Warehouse.models.forms.auth.RegisterForm;
import com.example.Warehouse.models.forms.category.CategorySearchForm;
import com.example.Warehouse.models.forms.product.ProductCreateForm;
import com.example.Warehouse.models.forms.product.ProductSearchForm;
import com.example.Warehouse.models.forms.product.ProductWarehouseSearchForm;
import com.example.Warehouse.models.forms.warehouse.WarehousesSearchForm;
import com.example.Warehouse.models.viewmodels.category.CategoryViewModel;
import com.example.Warehouse.models.viewmodels.order.OrderViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductInCartViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductStockViewModel;
import com.example.Warehouse.models.viewmodels.product.ProductViewModel;
import com.example.Warehouse.models.viewmodels.purchase.PurchaseViewModel;
import com.example.Warehouse.models.viewmodels.warehouse.WarehouseViewModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.registerModule(new RecordModule());

        //region Auth
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
        //endregion

        //region Category
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

        modelMapper.typeMap(CategoryDto.class, CategoryViewModel.class).setProvider(ctx -> {
            CategoryDto source = (CategoryDto) ctx.getSource();
            return new CategoryViewModel(
                source.id(),
                source.name(),
                source.discount(),
                source.isDeleted()
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

        //endregion

        //region Order
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

        modelMapper.typeMap(OrderDto.class, OrderViewModel.class).setProvider(ctx -> {
            OrderDto source = (OrderDto) ctx.getSource();
            return new OrderViewModel(
                source.number(),
                source.totalPrice(),
                source.date()
            );
        });
        //endregion

        //region Purchase
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

        modelMapper.typeMap(PurchaseDto.class, PurchaseViewModel.class).setProvider(ctx -> {
            PurchaseDto source = (PurchaseDto) ctx.getSource();
            return new PurchaseViewModel(
                source.number(),
                source.status(),
                source.cashback(),
                source.totalPrice(),
                source.date()
            );
        });
        //endregion

        //region Product
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

        modelMapper.typeMap(ProductCartDto.class, ProductInCartViewModel.class).setProvider(ctx -> {
            ProductCartDto source = (ProductCartDto) ctx.getSource();
            return new ProductInCartViewModel(
                source.id(),
                source.name(),
                source.category(),
                source.quantity(),
                source.totalPrice()
            );
        });

        modelMapper.typeMap(ProductDto.class, ProductViewModel.class).setProvider(ctx -> {
            ProductDto source = (ProductDto) ctx.getSource();
            return new ProductViewModel(
                source.id(),
                source.name(),
                source.price(),
                source.oldPrice(),
                source.category(),
                source.quantity(),
                source.isDeleted()
            );
        });

        modelMapper.typeMap(ProductCreateForm.class, ProductAddDto.class).setProvider(ctx -> {
            ProductCreateForm source = (ProductCreateForm) ctx.getSource();
            return new ProductAddDto(
                source.name(),
                source.category(),
                source.price()
            );
        });

        modelMapper.typeMap(ProductStockDto.class, ProductStockViewModel.class).setProvider(ctx -> {
            ProductStockDto source = (ProductStockDto) ctx.getSource();
            return new ProductStockViewModel(
                source.id(),
                source.name(),
                source.quantity(),
                source.minStock(),
                source.maxStock(),
                source.category(),
                source.isDeleted()
            );
        });
        //endregion

        //region Warehouse
        modelMapper.typeMap(WarehousesSearchForm.class, WarehouseSearchDto.class).setProvider(ctx -> {
            WarehousesSearchForm source = (WarehousesSearchForm) ctx.getSource();
            return new WarehouseSearchDto(
                source.pages().page(),
                source.pages().size(),
                source.pages().substring(),
                source.returnDeleted()
            );
        });

        modelMapper.typeMap(WarehouseDto.class, WarehouseViewModel.class).setProvider(ctx -> {
            WarehouseDto source = (WarehouseDto) ctx.getSource();
            return new WarehouseViewModel(
                source.id(),
                source.name(),
                source.location(),
                source.isDeleted()
            );
        });
        //endregion

        return modelMapper;
    }
}
