package com.example.ordersservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        // Spring Boot tự động cung cấp WebClient.Builder đã được cấu hình sẵn
        return builder.build();
    }
}