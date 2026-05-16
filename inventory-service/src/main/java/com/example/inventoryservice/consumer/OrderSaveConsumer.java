package com.example.inventoryservice.consumer;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.event.OrderEvent;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderSaveConsumer {

    private final InventoryRepository inventoryRepository;

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void handleOrderCreated(OrderEvent event) {
        log.info(">>> Inventory Service nhận được tin nhắn cho đơn hàng: {}", event.getOrderItems());

        for (var item : event.getOrderItems()) {

            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy kho cho SP này"));

            // Trừ stock_quantity
            inventory.setStockQuantity(inventory.getStockQuantity() - item.getQuantity());

            // Lưu lại
            inventoryRepository.save(inventory);
            log.info(">>> Đã trừ kho thành công cho sản phẩm: {}", item.getProductId());
        }
    }
}