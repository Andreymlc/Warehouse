package com.example.Warehouse.domain.models;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "purchase_items")
public class PurchaseItem extends BaseEntity {
    private Integer quantity;
    private Product product;
    private Purchase purchase;

    protected PurchaseItem() {}

    public PurchaseItem(Integer quantity, Product product, Purchase purchase) {
        this.quantity = quantity;
        this.product = product;
        this.purchase = purchase;
    }

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
}
