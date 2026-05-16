package com.example.promotionservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PromotionRequest {
    private String code;
    private Integer discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
}