package com.example.ordersservice.mapper;


import com.example.ordersservice.clients.dto.response.ProductRes;
import com.example.ordersservice.impl.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(ProductRes res) {
        if (res == null) {
            return null;
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(res.getId());
        productResponse.setPrice(res.getPrice());
        productResponse.setStock(res.getStock());
        return productResponse;
    }
}
