package com.example.Warehouse.domain.models;

import com.example.Warehouse.domain.enums.Status;
import jakarta.persistence.*;

import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
public class Purchase extends BaseEntity {
    private Status status;
    private String number;
    private Float totalPrice;
    private Integer cashback;
    private LocalDateTime date;
    private Integer pointsSpent;

    private User user;
    private List<PurchaseItem> purchaseItems;

    protected Purchase() {
    }

    public Purchase(
        User user,
        Status status,
        String number,
        Float totalPrice,
        Integer cashback,
        LocalDateTime date,
        Integer pointsSpent,
        List<PurchaseItem> purchaseItems
    ) {
        this.user = user;
        this.date = date;
        this.number = number;
        this.status = status;
        this.cashback = cashback;
        this.totalPrice = totalPrice;
        this.pointsSpent = pointsSpent;
        this.purchaseItems = purchaseItems;
    }

    @Column(name = "date", nullable = false)
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime purchaseDate) {
        this.date = purchaseDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void nextStatus() {
        this.status = this.status.next();
    }

    @Column(name = "total_price", nullable = false)
    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalAmount) {
        this.totalPrice = totalAmount;
    }

    @Column(name = "points_spent", nullable = false)
    public Integer getPointsSpent() {
        return pointsSpent;
    }

    public void setPointsSpent(Integer pointsSpent) {
        this.pointsSpent = pointsSpent;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "purchase")
    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    @Column(name = "number", unique = true)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "cashback")
    public Integer getCashback() {
        return cashback;
    }

    public void setCashback(Integer cashback) {
        this.cashback = cashback;
    }
}
