package com.example.notificationservice.consumer;

import com.example.notificationservice.dto.PromotionEvent;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromotionConsumer {


    private final NotificationRepository notificationRepository;
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            autoCreateTopics = "true",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            include = {RuntimeException.class}
    )
    @KafkaListener(topics = "promotion-notifications", groupId = "notification-group-final")
    public void consumePromotion(String message) {
        log.info(" [KAFKA RECEIVE] Đã nhận tin: {}", message);

        try {
            Notification notification = Notification.builder()
                    .message(message)
                    .title("Test Postman")
                    .type("PUSH")
                    .status("SUCCESS")
                    .createdAt(LocalDateTime.now())

                    .build();

            notificationRepository.save(notification);
            log.info("💾 [DATABASE] Đã lưu vào MySQL thành công!");
        } catch (Exception e) {
            // Log này sẽ cho bạn biết chính xác tại sao không lưu được vào DB
            log.error(" LỖI LƯU DB: {}", e.getMessage());
            e.printStackTrace();
        }
    }
    }
