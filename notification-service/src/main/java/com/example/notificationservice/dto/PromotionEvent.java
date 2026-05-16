package com.example.notificationservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionEvent {

    private Long promotionId;
    private String title;
    private String message;
    private Integer discount;
}