package com.example.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionEvent {
    private String eventId;
    private String promotionCode;
    private String message;
    private String targetGroup; // Ví dụ: VIP_USERS, ALL_USERS
    private Long productId;
}