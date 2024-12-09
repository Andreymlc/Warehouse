package com.example.Warehouse.domain.models;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    private Order order;
    private Product product;
    private Integer quantity;

    protected OrderItem() {}

    public OrderItem(
            Integer quantity,
            Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
