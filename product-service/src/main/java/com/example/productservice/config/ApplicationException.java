package com.example.productservice.config;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }
}