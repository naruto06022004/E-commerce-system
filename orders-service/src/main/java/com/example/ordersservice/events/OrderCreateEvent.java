package com.example.ordersservice.events;

import com.example.ordersservice.entity.Order;
import com.example.ordersservice.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.List;
@Setter
@Getter
@ToString
public class OrderCreateEvent extends Order {
    private List<OrderItem> orderItems;
}
