package com.example.Warehouse.domain.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String email;
    private Integer points;
    private String username;
    private String passwordHash;

    private List<Role> roles;
    private List<Order> orders;
    private List<CartItem> cart;
    private Set<Purchase> purchases;

    protected User() {
    }

    public User(
        String email,
        Integer points,
        String username,
        String passwordHash
    ) {
        this.email = email;
        this.points = points;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @Column(name = "username", nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @Column(name = "role", nullable = false)
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> role) {
        this.roles = role;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
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
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
