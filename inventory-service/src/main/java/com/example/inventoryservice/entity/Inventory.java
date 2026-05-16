package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;


    public Integer getQuantity() {
        return this.stockQuantity;
    }

    public void setQuantity(Integer quantity) {
        this.stockQuantity = quantity;
    }
}