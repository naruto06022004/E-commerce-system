package com.example.notificationservice.service;

import com.example.notificationservice.dto.EmailBatchEvent;
import com.example.notificationservice.dto.PromotionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void createEmailBatch(PromotionEvent event){

        List<String> emails = List.of(
                "user1@gmail.com",
                "user2@gmail.com",
                "user3@gmail.com"
        );

        EmailBatchEvent batchEvent = new EmailBatchEvent(
                emails,
                event.getTitle(),
                event.getMessage()
        );

        kafkaTemplate.send("email-batch-topic", batchEvent);
    }
}