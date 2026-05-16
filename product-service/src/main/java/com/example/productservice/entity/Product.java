package com.example.productservice.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String id;
    private String name;
    private String sku;
    private Double price;
    private Integer stock;

    // Thuộc tính này quan trọng khi dùng Kafka để đồng bộ kho
    private Boolean isAvailable;

}