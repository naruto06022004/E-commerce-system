package com.example.promotionservice.mapper;

import com.example.promotionservice.dto.PromotionRequest;
import com.example.promotionservice.dto.PromotionResponse;
import com.example.promotionservice.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // BẮT BUỘC: Để Spring có thể quản lý Bean này
public interface PromotionMapper {
    Promotion toEntity(PromotionRequest request);

    @Mapping(target = "message", ignore = true)
    PromotionResponse toResponse(Promotion promotion);
}