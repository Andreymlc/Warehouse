package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.models.BaseEntity;
import com.example.Warehouse.domain.models.OrderItem;
import com.example.Warehouse.domain.repository.contracts.cart.CartRepository;
import org.modelmapper.ModelMapper;
import com.example.Warehouse.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Order;
import com.example.Warehouse.domain.enums.Status;
import org.springframework.data.domain.PageRequest;
import com.example.Warehouse.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import com.example.Warehouse.domain.repository.contracts.user.UserRepository;
import com.example.Warehouse.domain.repository.contracts.order.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;

    public OrderServiceImpl(
        CartRepository cartRepo,
        ModelMapper modelMapper,
        UserRepository userRepo,
        OrderRepository orderRepo
    ) {
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDto getOrderByNumber(String number) {
        var order = orderRepo.findById(number).orElseThrow();
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Page<OrderDto> getOrders(String status, boolean dateSort, int page, int size) {
        Sort sort = dateSort ? Sort.by("date").ascending() : Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Order> orderPage;
        if(!status.isBlank()) {
            orderPage = orderRepo.findByStatus(Status.valueOf(status), pageable);
        } else {
            orderPage = orderRepo.findAll(pageable);
        }

        return orderPage.map(p ->
            new OrderDto(
                p.getId(),
                p.getStatus(),
                p.getOrderDate(),
                p.getTotalAmount()
            )
        );
    }

    @Override
    @Transactional
    public void addOrder(String userId) {
        var existingUser = userRepo.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        var orderItems = existingUser
            .getCart()
            .stream()
            .map(c -> new OrderItem(c.getQuantity(), c.getProduct()))
            .toList();

        float totalPrice = 0;
        for (var item : orderItems) {
            var product = item.getProduct();
            var category = product.getCategory();

            totalPrice += (float) Math.round(product.getPrice() * category.getDiscount() * 100) / 100 * item.getQuantity();
        }

        var order = new Order(
            existingUser,
            Status.CREATED,
            totalPrice,
            LocalDateTime.now(),
            orderItems
        );

        orderItems.forEach(item -> item.setOrder(order));

        orderRepo.save(order);

        cartRepo.deleteAll(existingUser.getCart());
    }
}
