package com.example.ordersservice.clients.dto.request;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderStockNotifyRequest {
    private String orderId;
    private String customerId;
    private List<OrderStockLineItem> items;
}
