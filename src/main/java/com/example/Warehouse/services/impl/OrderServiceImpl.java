package com.example.Warehouse.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.example.Warehouse.domain.models.Order;
import com.example.WarehouseContracts.enums.Status;
import org.springframework.data.domain.PageRequest;
import com.example.Warehouse.services.OrderService;
import com.example.WarehouseContracts.dto.OrderDto;
import jakarta.persistence.EntityNotFoundException;
import com.example.Warehouse.domain.repository.UserRepository;
import com.example.Warehouse.domain.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepo;
    private final OrderRepository orderRepo;

    public OrderServiceImpl(
        OrderRepository orderRepo,
        ModelMapper modelMapper,
        UserRepository userRepo
    ) {
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
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
    public String addOrder(String userId, String status, BigDecimal price) {
        var existingUser = userRepo.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        return orderRepo.save(
            new Order(
                existingUser,
                Status.valueOf(status),
                price,
                LocalDateTime.now()
            )
        ).getId();
    }
}
