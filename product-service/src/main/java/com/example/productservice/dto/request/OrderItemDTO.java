package com.example.productservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderItemDTO {
    @NotEmpty
    private String productId;
    @NotNull
    private Integer quantity;

}
