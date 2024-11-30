package com.example.Warehouse.domain.models;

import jakarta.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock extends BaseEntity {
    private Integer quantity;
    private Integer minimumStock;

    private Product product;
    private Warehouse warehouse;

    protected Stock() {}

    public Stock(Integer quantity, Integer minimumStock, Product product, Warehouse warehouse) {
        this.product = product;
        this.quantity = quantity;
        this.warehouse = warehouse;
        this.minimumStock = minimumStock;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
