package com.example.promotionservice.controller;

import com.example.promotionservice.dto.PromotionRequest;
import com.example.promotionservice.dto.PromotionResponse;
import com.example.promotionservice.producer.PromotionProducer;
import com.example.promotionservice.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {


    private final PromotionService promotionService;
    private final PromotionProducer promotionProducer;


    @PostMapping
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody PromotionRequest request) {
        PromotionResponse response = promotionService.createPromotion(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/apply")
    public ResponseEntity<?> applyPromotion(@RequestParam String code) {
        Integer discount = promotionService.getDiscountRate(code);
        if (discount != null) {
            return ResponseEntity.ok(discount);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Mã giảm giá không hợp lệ hoặc đã hết hạn");
    }


    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcastPromotion(@RequestBody String message) {
        promotionProducer.sendBlackFridayAlert(message);
        return ResponseEntity.ok("Chiến dịch thông báo đã được kích hoạt thành công!");
    }
}