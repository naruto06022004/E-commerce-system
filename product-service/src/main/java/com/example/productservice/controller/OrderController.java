package com.example.productservice.controller;



import com.example.productservice.dto.request.CreateOrderRequest;
import com.example.productservice.dto.response.OrderResponse;
import com.example.productservice.entity.Product;

import com.example.productservice.service.OderService;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OderService oderService;
    private final ProductService productService; // 1. Thêm khai báo này

    // 2. Cập nhật Constructor để Spring tiêm ProductService vào
    public OrderController(OderService oderService, ProductService productService) {
        this.oderService = oderService;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = oderService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable String id) {
        return productService.getById(id); // Bây giờ dòng này mới chạy được
    }
}