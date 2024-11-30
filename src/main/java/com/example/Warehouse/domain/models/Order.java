package com.example.Warehouse.domain.models;

import com.example.WarehouseContracts.enums.Status;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private User user;
    private Status status;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private Set<OrderItem> orderItems;

    protected Order() {}

    public Order(
            User user,
            Status status,
            BigDecimal totalAmount,
            LocalDateTime orderDate) {
        this.user = user;
        this.status = status;
        this.orderDate = orderDate;
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
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "order")
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
