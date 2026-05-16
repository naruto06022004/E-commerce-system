package com.example.productservice.controller;


import com.example.productservice.common.BaseResponse;
import com.example.productservice.config.UserNotFoundException;
import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.request.OrderStockNotifyRequest;
import com.example.productservice.dto.request.ProductFilter;
import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable String id, @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {

        productService.softDelete(id);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable String id) {
        if (id == null) {
            throw new UserNotFoundException("User không tồn tại");
    }
    return productService.getById(id);}

    @PostMapping("/search")
    public BaseResponse<List<ProductResponse>> search(@RequestBody ProductFilter filter) {
        List<ProductResponse> results = productService.search(filter);
        System.out.println(">>> Search found: " + results.size() + " items"); // Thêm dòng này
        return BaseResponse.ok(results);
    }

    @PostMapping("/internal/order-stock")
    public ResponseEntity<Void> updateStock(@RequestBody OrderStockNotifyRequest request) {
        System.out.println(">>> ĐÃ NHẬN REQUEST: " + request.getOrderId());
        productService.decreaseStock(request);
        return ResponseEntity.ok().build();
    }
}
