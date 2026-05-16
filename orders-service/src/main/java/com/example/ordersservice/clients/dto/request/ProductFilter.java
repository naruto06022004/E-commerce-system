package com.example.ordersservice.clients.dto.request;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilter {
    private List<String> ids;
}
