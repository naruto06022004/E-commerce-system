package com.example.ordersservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id ;
    private String customerId ;
    private Integer totalAmount ;
    private Instant createdDate;
    private String createdBy ;
    private Instant lastModifiedDate;
    private String lastModifiedBy ;
    private Boolean isDeleted ;
}
