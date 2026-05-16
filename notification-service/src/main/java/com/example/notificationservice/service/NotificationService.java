package com.example.notificationservice.service;

import com.example.notificationservice.dto.PromotionEvent;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BatchService batchService;

    public void processPromotion(PromotionEvent event){

        batchService.createEmailBatch(event);

        Notification notification = Notification.builder()
                .title(event.getTitle())
                .message(event.getMessage())
                .status("PENDING")
                .type("EMAIL")
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }
}