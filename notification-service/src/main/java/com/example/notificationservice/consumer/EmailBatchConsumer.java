package com.example.notificationservice.consumer;

import com.example.notificationservice.dto.EmailBatchEvent;
import com.example.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailBatchConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "email-batch-topic",
            groupId = "email-group"
    )
    public void consumeBatch(EmailBatchEvent event){
        emailService.sendBatch(event);
    }
}