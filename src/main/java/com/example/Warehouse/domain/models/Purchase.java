package com.example.Warehouse.domain.models;

import com.example.Warehouse.domain.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "purchases")
public class Purchase extends BaseEntity {
    private LocalDateTime purchaseDate;
    private Float totalAmount;
    private Integer pointsSpent;
    private Status status;
    private User user;
    private Set<PurchaseItem> purchaseItems;

    protected Purchase() {}

    public Purchase(
            LocalDateTime purchaseDate,
            Float totalAmount,
            Integer pointsSpent,
            Status status,
            User user,
            Set<PurchaseItem> purchaseItems) {
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
        this.pointsSpent = pointsSpent;
        this.status = status;
        this.user = user;
        this.purchaseItems = purchaseItems;
    }

    @Column(name = "purchase_date", nullable = false)
    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    @Column(name = "points_spent", nullable = false)
    public Integer getPointsSpent() {
        return pointsSpent;
    }

    public void setPointsSpent(Integer pointsSpent) {
        this.pointsSpent = pointsSpent;
    }

    @ManyToOne(optional = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "purchase")
    public Set<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(Set<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }
}
