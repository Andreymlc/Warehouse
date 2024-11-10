package com.example.Warehouse.domain.models;

import com.example.Warehouse.domain.enums.Roles;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String userName;
    private String email;
    private String passwordHash;
    private Roles role;
    private Integer points;
    private Set<Purchase> purchases;

    protected User() {}

    public User(
            String userName,
            String email,
            String passwordHash,
            Roles role,
            Integer points) {
        this.userName = userName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.points = points;
    }

    @Column(name = "user_name", nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Column(name = "email", nullable = false, unique = true)
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

    @OneToMany(mappedBy = "user")
    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }
}
