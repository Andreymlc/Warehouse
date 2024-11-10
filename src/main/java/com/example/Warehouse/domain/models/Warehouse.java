package com.example.Warehouse.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "warehouses")
public class Warehouse extends BaseEntity {
    private String name;
    private String location;
    private Set<Stock> stock;

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

    @OneToMany(mappedBy = "warehouse")
    public Set<Stock> getStocks() {
        return stock;
    }

    public void setStocks(Set<Stock> stock) {
        this.stock = stock;
    }
}
