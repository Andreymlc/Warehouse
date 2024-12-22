package com.example.Warehouse.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchase_items")
public class PurchaseItem extends BaseEntity {
    private Product product;
    private Integer quantity;
    private Float totalPrice;
    private Purchase purchase;

    protected PurchaseItem() {
    }

    public PurchaseItem(
        Product product,
        Integer quantity,
        Float totalPrice
    ) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @ManyToOne(optional = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(optional = false)
    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    @Column(name = "total_price", nullable = false)
    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
