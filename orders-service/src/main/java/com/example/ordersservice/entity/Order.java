package com.example.ordersservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;
    private String orderCode;
    private String customerId;
    private Long userId;
    private int totalAmount;
    private String orderStatus;

}