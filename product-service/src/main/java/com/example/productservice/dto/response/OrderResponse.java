package com.example.productservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class OrderResponse {
    private String id;
    private String customerId;
            private String productId;
    private Integer totalAmount;
    private Integer createdDate;
    private String createdBy;
    private Instant lastModifiedDate;
    private String lastModifiedBy;
}
