package com.example.Warehouse.domain.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    private String name;
    private Float discount;
    private boolean isDeleted;

    private Set<Product> products;

    protected Category() {}

    public Category(
        String name,
        Float discount,
        boolean isDeleted
    ) {
        this.name = name;
        this.discount = discount;
        this.isDeleted = isDeleted;
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "discount", nullable = false)
    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    @OneToMany(mappedBy = "category", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Column(name = "is_deleted", nullable = false)
    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
