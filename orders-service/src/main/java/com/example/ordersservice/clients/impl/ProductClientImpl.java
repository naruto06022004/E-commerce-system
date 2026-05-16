package com.example.ordersservice.clients.impl;

import com.example.ordersservice.clients.ProductClient;
import com.example.ordersservice.clients.dto.request.OrderStockNotifyRequest;
import com.example.ordersservice.clients.dto.request.ProductFilter;
import com.example.ordersservice.clients.dto.response.ProductRes;
import com.example.ordersservice.common.BaseResponse;
import com.example.ordersservice.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${services.product.base-url:http://localhost:8081}")
    private String productBaseUrl;


    public List<ProductRes> search(ProductFilter filter) {
        try {
            BaseResponse<List<ProductRes>> response = webClientBuilder
                    .baseUrl(productBaseUrl)
                    .build()
                    .post()
                    .uri("/api/v1/products/search")
                    .bodyValue(filter)
                    .retrieve()
                    .onStatus(status -> status.isError(), res ->
                            res.bodyToMono(String.class)
                                    .defaultIfEmpty("EMPTY_BODY")
                                    .flatMap(body -> Mono.error(new ApplicationException("Product Service error: " + body)))
                                    .map(body -> {
                                        System.out.println("🔥 PRODUCT ERROR STATUS: " + res.statusCode());
                                        System.out.println("🔥 PRODUCT ERROR BODY: " + body);
                                        System.err.println("🔥 LỖI TỪ PRODUCT SERVICE: " + body);
                                        return new RuntimeException("Product-service failed: " + body);
                                    })
                    )

                    .bodyToMono(new ParameterizedTypeReference<BaseResponse<List<ProductRes>>>() {})
                    .block();

            if (response == null) {
                throw new ApplicationException("Product-service returned null response");
            }

            if (response.getData() == null) {
                throw new ApplicationException("Product-service data is null");
            }

            return response.getData();

        } catch (Exception e) {
            throw new ApplicationException("Product-service error: " + e.getMessage());
        }
    }

    public void notifyOrderStock(OrderStockNotifyRequest request) {
        try {
            webClientBuilder
                    .baseUrl(productBaseUrl)
                    .build()
                    .post()
                    .uri("/api/v1/products/internal/order-stock")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), res ->
                            res.bodyToMono(String.class)
                                    .defaultIfEmpty("Không có nội dung lỗi (Empty Body)")
                                    .flatMap(body -> {
                                        // In ra Console ngay lập tức để bạn nhìn thấy
                                        System.err.println("❌ LỖI TỪ PRODUCT SERVICE (Stock Update):");
                                        System.err.println("❌ Status code: " + res.statusCode());
                                        System.err.println("❌ Chi tiết lỗi: " + body);

                                        return Mono.error(new ApplicationException("Product Service error: " + body));
                                    })
                    )
                    .toBodilessEntity()
                    .block();

            System.out.println("✅ Cập nhật kho bên Product Service thành công!");

        } catch (Exception e) {
            // Log lại lần nữa để chắc chắn bạn không bỏ lỡ thông tin trong stack trace
            System.err.println("🔥 Exception caught: " + e.getMessage());
            throw new ApplicationException("Lỗi khi gọi Product Service: " + e.getMessage());
        }
    }
}

