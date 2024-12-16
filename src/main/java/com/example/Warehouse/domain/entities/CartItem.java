package com.example.Warehouse.domain.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
    private Integer quantity = 0;

    private User user;
    private Product product;

    protected CartItem() {}

    public CartItem(
        User user,
        Product product,
        Integer quantity
    ) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @ManyToOne(optional = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem cartItem)) return false;
        return product.equals(cartItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(product);
    }

    public void incrQuantity() {
        this.quantity++;
    }

    public void decrQuantity() {
        this.quantity--;
    }
}
