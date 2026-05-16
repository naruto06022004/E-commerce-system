package com.example.productservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrderRequest {
    @NotEmpty
    private String productId;


    @NotNull
    @Valid
    private List <OrderItemDTO> OrderItems;

}
