package com.example.ordersservice.clients;



import com.example.ordersservice.clients.dto.request.OrderStockNotifyRequest;
import com.example.ordersservice.clients.dto.request.ProductFilter;
import com.example.ordersservice.clients.dto.response.ProductRes;

import java.util.List;

public interface ProductClient {
    List<ProductRes> search(ProductFilter filter);

    void notifyOrderStock(OrderStockNotifyRequest request);
}

