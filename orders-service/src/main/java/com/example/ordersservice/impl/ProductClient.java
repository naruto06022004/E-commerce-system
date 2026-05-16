package com.example.ordersservice.impl; // Đổi package cho đúng thư mục clients

// Import DTO này vào
import java.util.List;

public interface ProductClient {
    void notifyOrderStock(List<OrderItemRequest> requests);
}