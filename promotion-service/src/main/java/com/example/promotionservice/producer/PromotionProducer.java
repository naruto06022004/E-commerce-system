package com.example.promotionservice.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PromotionProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendBlackFridayAlert(String message) {
        kafkaTemplate.send("promotion-notifications", message);
    }
}