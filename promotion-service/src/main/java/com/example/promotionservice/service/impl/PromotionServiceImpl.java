package com.example.promotionservice.service.impl;

import com.example.promotionservice.dto.PromotionRequest;
import com.example.promotionservice.dto.PromotionResponse;
import com.example.promotionservice.entity.Promotion;
import com.example.promotionservice.mapper.PromotionMapper;
import com.example.promotionservice.repository.PromotionRepository;
import com.example.promotionservice.service.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j // Dùng cái này để in log ra màn hình console
public class PromotionServiceImpl implements PromotionService {

    private final StringRedisTemplate redisTemplate;
    private final PromotionMapper promotionMapper;
    private final PromotionRepository promotionRepository;

    private static final String PROMO_KEY_PREFIX = "promo:code:";

    @Override
    public PromotionResponse createPromotion(PromotionRequest request) {
        Promotion promotion = promotionMapper.toEntity(request);
        promotion = promotionRepository.save(promotion);

        // Đẩy vào Redis ngay khi tạo để người dùng dùng được luôn
        syncPromotionToRedis(promotion.getCode(), promotion.getDiscountPercentage());

        return promotionMapper.toResponse(promotion);
    }

    @Override
    public Integer getDiscountRate(String code) {
        // 1. Thử lấy từ Redis
        String rateStr = redisTemplate.opsForValue().get(PROMO_KEY_PREFIX + code);

        if (rateStr != null) {
            log.info(" Redis Hit! Lấy mã {} từ Cache", code);
            return Integer.parseInt(rateStr);
        }

        // 2. Nếu Redis trống, tìm trong MySQL
        log.info("🐢 Redis Miss! Đang tìm mã {} trong MySQL", code);
        return promotionRepository.findByCode(code)
                .map(promo -> {
                    // 3. Cập nhật lại Redis cho lần sau
                    syncPromotionToRedis(code, promo.getDiscountPercentage());
                    return promo.getDiscountPercentage();
                })
                .orElse(0); // Trả về 0 nếu không tìm thấy
    }

    @Override
    public void syncPromotionToRedis(String code, Integer rate) {
        if (code != null && rate != null) {
            redisTemplate.opsForValue().set(
                    PROMO_KEY_PREFIX + code,
                    String.valueOf(rate),
                    Duration.ofMinutes(10) // Cache 10 phút
            );
            log.info(" Đã đồng bộ mã {} vào Redis", code);
        }
    }
}