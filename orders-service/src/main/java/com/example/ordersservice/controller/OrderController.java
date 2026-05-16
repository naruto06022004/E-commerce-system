package com.example.ordersservice.controller;



import com.example.ordersservice.dto.request.CreateOrderRequest;
import com.example.ordersservice.dto.response.OrderResponse;
import com.example.ordersservice.impl.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController

@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest requestOrderDTO ){
        OrderResponse response = orderService.create(requestOrderDTO);
        return ResponseEntity.ok(response);
    }
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
}
