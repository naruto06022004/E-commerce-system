package com.example.ordersservice.impl;


import com.example.ordersservice.dto.request.CreateOrderRequest;
import com.example.ordersservice.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse create(CreateOrderRequest createOrderRequest) ;



}
