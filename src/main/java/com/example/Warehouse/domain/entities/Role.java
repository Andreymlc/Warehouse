package com.example.Warehouse.domain.entities;

import com.example.Warehouse.domain.enums.Roles;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private Roles name;

    public Role(Roles name) {
        this.name = name;
    }

    protected Role() {
    }

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    public Roles getName() {
        return name;
    }

    public void setName(Roles name) {
        this.name = name;
    }
}
