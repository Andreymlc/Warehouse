package com.example.Warehouse.domain.models;

import jakarta.persistence.*;
import com.example.Warehouse.domain.enums.Roles;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private Roles role;
    private String email;
    private Integer points;
    private String userName;
    private String passwordHash;

    private Set<Order> orders;
    private List<CartItem> cart;
    private Set<Purchase> purchases;

    protected User() {}

    public User(
        Roles role,
        String email,
        Integer points,
        String userName,
        List<CartItem> cart,
        String passwordHash) {
        this.cart = cart;
        this.role = role;
        this.email = email;
        this.points = points;
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    @Column(name = "user_name", nullable = false, unique = true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password_hash", nullable = false)
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Column(name = "points", nullable = false)
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    @OneToMany(mappedBy = "user")
    public List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }

    public void addCartItem(CartItem cartItem) {
        this.cart.add(cartItem);
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
