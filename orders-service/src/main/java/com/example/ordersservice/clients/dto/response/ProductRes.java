package com.example.ordersservice.clients.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRes {

    private String id;
    private double price;
    private int stock;

    public ProductRes() {}

    public ProductRes(String id, double price, int stock) {
        this.id = id;
        this.price = price;
        this.stock = stock;
    }
}