package com.example.Warehouse.domain.models;

import com.example.Warehouse.domain.enums.Status;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private Status status;
    private Float totalAmount;
    private LocalDateTime orderDate;

    private User user;
    private List<OrderItem> orderItems;

    protected Order() {}

    public Order(
            User user,
            Status status,
            Float totalAmount,
            LocalDateTime orderDate,
            List<OrderItem> orderItems
    ) {
        this.user = user;
        this.status = status;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
    }

    @Column(name = "order_date", nullable = false)
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(name = "total_amount", nullable = false)
    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
