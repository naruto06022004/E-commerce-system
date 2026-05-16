package com.example.productservice.config;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllException(Exception ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse ("USER_NOT_FOUND", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(
                new  ErrorResponse ("USER_NOT_FOUND", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.BAD_REQUEST
                );
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("USER_NOT_FOUND", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.NOT_FOUND
        );
}
}