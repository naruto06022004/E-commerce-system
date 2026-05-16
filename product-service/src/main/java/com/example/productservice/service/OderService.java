package com.example.productservice.service;


import com.example.productservice.dto.request.CreateOrderRequest;
import com.example.productservice.dto.response.OrderResponse;

public interface OderService {
    OrderResponse create(CreateOrderRequest request);
}
