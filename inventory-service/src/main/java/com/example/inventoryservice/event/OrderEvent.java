package com.example.inventoryservice.event;

import com.example.inventoryservice.event.OrderItemEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String id;
    private List<OrderItemEvent> orderItems;
}