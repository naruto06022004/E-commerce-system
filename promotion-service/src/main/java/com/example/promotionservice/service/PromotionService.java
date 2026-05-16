package com.example.promotionservice.service;

import com.example.promotionservice.dto.PromotionRequest;
import com.example.promotionservice.dto.PromotionResponse;
import org.springframework.cache.annotation.Cacheable;

public interface PromotionService {

    Integer getDiscountRate(String code);
    void syncPromotionToRedis(String code, Integer rate);
    PromotionResponse createPromotion(PromotionRequest request);
}