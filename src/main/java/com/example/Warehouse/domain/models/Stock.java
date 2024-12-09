package com.example.Warehouse.domain.models;

import jakarta.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock extends BaseEntity {
    private Integer quantity;
    private Integer minStock;
    private Integer maxStock;

    private Product product;
    private Warehouse warehouse;

    protected Stock() {}

    public Stock(Integer quantity, Integer minStock, Integer maxStock, Product product, Warehouse warehouse) {
        this.maxStock = maxStock;
        this.product = product;
        this.quantity = quantity;
        this.warehouse = warehouse;
        this.minStock = minStock;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "minimum_stock", nullable = false)
    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minimumStock) {
        this.minStock = minimumStock;
    }

    @Column(name = "maximum_stock", nullable = false)
    public Integer getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }

    @ManyToOne(optional = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(optional = false)
    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouses) {
        this.warehouse = warehouses;
    }
}
