package com.example.productservice.config;

import org.springframework.http.HttpStatus;

public class ErrorResponse  {

    private String code;
    private String message;

    public ErrorResponse(String code, String message, HttpStatus internalServerError) {
        this.code = code;
        this.message = message;
    }

    // getter
}