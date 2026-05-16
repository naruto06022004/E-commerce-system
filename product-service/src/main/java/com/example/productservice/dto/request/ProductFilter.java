package com.example.productservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductFilter {
    private List<String> ids;

    public ProductFilter() {
    }

    public ProductFilter(List<String> ids) {
        this.ids = ids;
    }

}

