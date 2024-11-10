package com.example.Warehouse.domain.models;

import jakarta.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock extends BaseEntity {
    private Integer quantity;
    private String lastUpdated;
    private Integer minimumStock;
    private Product product;
    private Warehouse warehouse;

    protected Stock() {}

    public Stock(Integer quantity, String lastUpdated, Integer minimumStock, Product product, Warehouse warehouse) {
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
        this.minimumStock = minimumStock;
        this.product = product;
        this.warehouse = warehouse;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "last_updated", nullable = false)
    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Column(name = "minimum_stock", nullable = false)
    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouses) {
        this.warehouse = warehouses;
    }
}
