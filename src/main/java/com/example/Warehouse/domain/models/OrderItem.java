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
            Order order,
            Product product) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
