package com.example.inventoryservice.consumer;

import com.example.inventoryservice.event.OrderEvent;
import com.example.inventoryservice.event.OrderItemEvent;
import com.example.inventoryservice.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final InventoryRepository inventoryRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order-created", groupId = "inventory-group")

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void consumeOrder(String message) {
        try {
            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);
            log.info(">>> Đang xử lý đơn hàng ID: {}", orderEvent.getId());

            if (orderEvent.getOrderItems() != null) {
                for (OrderItemEvent item : orderEvent.getOrderItems()) {
                    log.info(">>> Kiểm tra sản phẩm ID: {} với số lượng: {}", item.getProductId(), item.getQuantity());

                    inventoryRepository.findByProductId(item.getProductId())
                            .ifPresentOrElse(inventory -> {
                                if (inventory.getQuantity() >= item.getQuantity()) {
                                    inventory.setQuantity(inventory.getQuantity() - item.getQuantity());
                                    inventoryRepository.save(inventory);
                                    log.info(" THÀNH CÔNG: Đã trừ kho sản phẩm {}", item.getProductId());
                                } else {
                                    log.warn(" THẤT BẠI: Sản phẩm {} không đủ số lượng", item.getProductId());
                                }
                            }, () -> log.error(" THẤT BẠI: Không tìm thấy productId '{}' trong DB", item.getProductId()));
                }
            }
        } catch (Exception e) {
            log.error(" Lỗi parse JSON: {}", e.getMessage());
        }
    }
    }


