package com.example.productservice.service.impl;

import com.example.productservice.dto.request.CreateOrderRequest;
import com.example.productservice.dto.response.OrderResponse;
import com.example.productservice.service.OderService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OderService {

    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {

        return new OrderResponse();
    }
}