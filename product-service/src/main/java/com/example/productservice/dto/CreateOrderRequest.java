package com.example.productservice.dto;


import com.example.productservice.dto.request.OrderItemDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;


import java.util.List;

public class CreateOrderRequest {
    @NotEmpty
    private String customerId;
    @NotEmpty
    @Valid
    private List<OrderItemDTO> orderItems;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
