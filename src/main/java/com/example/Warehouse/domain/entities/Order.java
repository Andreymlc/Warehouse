package com.example.Warehouse.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private String number;
    private Float totalAmount;
    private LocalDateTime date;

    private User user;
    private Warehouse warehouse;
    private List<OrderItem> orderItems;

    protected Order() {
    }

    public Order(
        User user,
        String number,
        Float totalAmount,
        LocalDateTime date,
        Warehouse warehouse,
        List<OrderItem> orderItems
    ) {
        this.user = user;
        this.date = date;
        this.number = number;
        this.warehouse = warehouse;
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
    }

    @Column(name = "date", nullable = false)
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime orderDate) {
        this.date = orderDate;
    }

    @Column(name = "number", nullable = false, unique = true)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
