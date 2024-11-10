package com.example.Warehouse.domain.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private String name;
    private BigDecimal price;
    private Category category;
    private Set<Stock> stocks;
    private Set<PurchaseItem> purchaseItems;
    private Set<OrderItem> orderItems;

    protected Product() {}

    public Product(
            String name,
            BigDecimal price,
            Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "price", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @OneToMany(mappedBy = "product")
    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stock) {
        this.stocks = stock;
    }

    @OneToMany(mappedBy = "product")
    public Set<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(Set<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    @OneToMany(mappedBy = "product")
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
