package com.example.productservice.dto.request;

import java.util.List;

public class OrderStockNotifyRequest {
    private String orderId;
    private String customerId;
    private List<OrderStockLineItem> items;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderStockLineItem> getItems() {
        return items;
    }

    public void setItems(List<OrderStockLineItem> items) {
        this.items = items;
    }
}
