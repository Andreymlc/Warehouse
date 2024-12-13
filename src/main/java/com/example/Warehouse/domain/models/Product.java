package com.example.Warehouse.domain.models;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private String name;
    private Float price;
    private boolean isDeleted;

    private Category category;
    private Set<Stock> stocks;
    private Set<CartItem> cartItems;
    private Set<OrderItem> orderItems;
    private Set<PurchaseItem> purchaseItems;

    protected Product() {}

    public Product(
            String name,
            Float price,
            Category category,
            boolean isDeleted
    ) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.isDeleted = isDeleted;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "price", nullable = false)
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stock) {
        this.stocks = stock;
    }

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    public Set<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(Set<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @OneToMany(mappedBy = "product")
    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return name.equalsIgnoreCase(product.name) && price.equals(product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Column(name = "is_deleted", nullable = false)
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
