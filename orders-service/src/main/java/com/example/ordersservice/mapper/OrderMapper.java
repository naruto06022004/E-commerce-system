package com.example.ordersservice.mapper;


import com.example.ordersservice.dto.response.OrderResponse;
import com.example.ordersservice.entity.Order;
import com.example.ordersservice.entity.OrderItem;
import com.example.ordersservice.events.OrderCreateEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {


    OrderResponse toOrderResponse(Order order, List<OrderItem> items);

  /*  @Mapping(target = "orderId", source = "order.id")*/
    @Mapping(target = "customerId", source = "order.customerId")
    @Mapping(target = "totalAmount", source = "order.totalAmount")
    @Mapping(target = "orderItems", source = "items")
    OrderCreateEvent toEvent(Order order, List<OrderItem> items);
}