package com.example.ordersservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
@Setter
@Getter
@Entity
public class OrderItem extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;
}

