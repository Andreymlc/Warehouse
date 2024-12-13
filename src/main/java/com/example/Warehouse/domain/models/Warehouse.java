package com.example.Warehouse.domain.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "warehouses")
public class Warehouse extends BaseEntity {
    private String name;
    private String location;
    private boolean isDeleted;

    private Set<Stock> stock;
    private Set<Order> orders;

    protected Warehouse() {}

    public Warehouse(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Warehouse(String name, String location, Set<Stock> stock) {
        this.name = name;
        this.location = location;
        this.stock = stock;
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "location", nullable = false)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.REMOVE)
    public Set<Stock> getStocks() {
        return stock;
    }

    public void setStocks(Set<Stock> stock) {
        this.stock = stock;
    }

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.REMOVE)
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Column(name = "is_deleted", nullable = false)
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
