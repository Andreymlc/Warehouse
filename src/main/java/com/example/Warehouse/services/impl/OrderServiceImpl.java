package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.models.Order;
import com.example.Warehouse.domain.models.OrderItem;
import com.example.Warehouse.domain.repositories.contracts.order.OrderRepository;
import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
import com.example.Warehouse.dto.OrderDto;
import com.example.Warehouse.dto.OrderItemDto;
import com.example.Warehouse.services.OrderService;
import com.example.Warehouse.services.WarehouseService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final ModelMapper modelMapper;
    private final UserRepository userRepo;
    private final OrderRepository orderRepo;
    private final WarehouseRepository warehouseRepo;
    private final WarehouseService warehouseService;

    public OrderServiceImpl(
        ModelMapper modelMapper,
        UserRepository userRepo,
        OrderRepository orderRepo,
        WarehouseService warehouseService,
        WarehouseRepository warehouseRepo
    ) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
        this.warehouseRepo = warehouseRepo;
        this.warehouseService = warehouseService;
    }

    @Override
    public OrderDto findOrderByNumber(String number) {
        var order = orderRepo.findById(number).orElseThrow();
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Page<OrderDto> findOrders(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("date").descending());

        var orderPage = orderRepo.findByUserId(userId, pageable);

        return orderPage.map(o -> modelMapper.map(o, OrderDto.class));
    }

    public List<OrderDto> findOrdersByUserId(String userId, int page, int size) {

        var existingUser = userRepo.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Пользоватль не найден"));

        return existingUser
            .getOrders()
            .stream()
            .map(o -> modelMapper.map(o, OrderDto.class))
            .toList();
    }

    @Override
    @Transactional
    public void addOrder(String userId, String warehouseId) {
        var existingUser = userRepo.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        var existingWarehouse = warehouseRepo.findById(warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("Склад не найден"));

        var orderItems = existingUser
            .getCart()
            .stream()
            .map(c -> new OrderItem(c.getProduct(), c.getQuantity()))
            .toList();

        float totalPrice = 0;
        for (var item : orderItems) {
            var product = item.getProduct();
            var category = product.getCategory();

            totalPrice += (float) Math.round(product.getPrice() * category.getDiscount() * 100) / 100 * item.getQuantity();
        }

        var order = new Order(
            existingUser,
            generateOrderNumber(),
            totalPrice,
            LocalDateTime.now(),
            existingWarehouse,
            orderItems
        );

        orderItems.forEach(item -> item.setOrder(order));
        existingUser.getOrders().add(order);

        warehouseService.fill(
            warehouseId,
            orderItems
                .stream()
                .map(oi -> new OrderItemDto(
                    oi.getProduct().getId(),
                    oi.getQuantity())
                ).toList()
        );

        existingUser.getCart().clear();
        userRepo.save(existingUser);
    }

    private String generateOrderNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
