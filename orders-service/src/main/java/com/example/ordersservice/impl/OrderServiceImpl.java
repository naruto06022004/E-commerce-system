package com.example.ordersservice.impl;
import com.example.ordersservice.clients.ProductClient;
import com.example.ordersservice.clients.dto.request.OrderStockLineItem;
import com.example.ordersservice.clients.dto.request.OrderStockNotifyRequest;
import com.example.ordersservice.clients.dto.request.ProductFilter;
import com.example.ordersservice.clients.dto.response.ProductRes;
import com.example.ordersservice.common.OrderStatus;
import com.example.ordersservice.dto.request.CreateOrderRequest;
import com.example.ordersservice.dto.request.OrderItemDto;
import com.example.ordersservice.dto.response.OrderResponse;
import com.example.ordersservice.entity.Order;
import com.example.ordersservice.entity.OrderItem;
import com.example.ordersservice.events.OrderCreateEvent;
import com.example.ordersservice.exception.ApplicationException;
import com.example.ordersservice.mapper.OrderMapper;
import com.example.ordersservice.mapper.ProductMapper;
import com.example.ordersservice.repository.OrderItemRepository;
import com.example.ordersservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    @Override
    public OrderResponse create(CreateOrderRequest request) {
        // 1. Validate input
        if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
            throw new ApplicationException("Order items must not be empty");
        }

        //  Lấy thông tin sản phẩm (Đồng bộ)
        List<String> productIds = request.getOrderItems().stream()
                .map(OrderItemDto::getProductId)
                .distinct()
                .toList();

        List<ProductRes> productResList = productClient.search(ProductFilter.builder().ids(productIds).build());

        if (productResList == null || productResList.isEmpty()) {
            throw new ApplicationException("No products found from Product Service");
        }

        Map<String, ProductRes> productMap = productResList.stream()
                .collect(Collectors.toMap(ProductRes::getId, p -> p));

        // Khởi tạo đối tượng Order (Chưa save vội để tránh gọi DB nhiều lần)
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setOrderStatus(OrderStatus.NEW.name());

        long total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDto dto : request.getOrderItems()) {
            ProductRes product = productMap.get(dto.getProductId());
            if (product == null) throw new ApplicationException("Product not found: " + dto.getProductId());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(dto.getProductId());
            item.setQuantity(dto.getQuantity());
            item.setPrice(product.getPrice());

            total += (long) product.getPrice() * dto.getQuantity();
            orderItems.add(item);
        }

        order.setTotalAmount((int) total);

        // 4. Lưu một lần duy nhất (Cascade sẽ tự lưu OrderItems)
        Order savedOrder = orderRepository.save(order);
        log.info(">>> Order saved with ID: {}", savedOrder.getId());

        // 5. Gọi Product Service trừ kho (Đồng bộ - Chạy TRƯỚC Kafka)

        try {
            OrderStockNotifyRequest stockNotify = OrderStockNotifyRequest.builder()
                    .orderId(savedOrder.getId())
                    .customerId(savedOrder.getCustomerId())
                    .items(orderItems.stream()
                            .map(oi -> new OrderStockLineItem(oi.getProductId(), oi.getQuantity()))
                            .toList())
                    .build();
            productClient.notifyOrderStock(stockNotify);
        } catch (Exception e) {
            log.error("Product Service stock update failed: {}", e.getMessage());
            throw new ApplicationException("Update stock failed, rolling back order: " + e.getMessage());
        }

        // 6. Gửi sự kiện Kafka (Bất đồng bộ - Chạy CUỐI CÙNG)
        try {
            OrderCreateEvent event = orderMapper.toEvent(savedOrder, orderItems);
            kafkaTemplate.send("order-created", event);
            log.info(">>> Kafka event sent for order: {}", savedOrder.getId());
        } catch (Exception e) {
            log.error("Failed to send Kafka event: {}", e.getMessage());

        }

        return orderMapper.toOrderResponse(savedOrder, orderItems);
    }
}