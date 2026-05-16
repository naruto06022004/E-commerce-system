package com.example.productservice.common;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public BaseResponse() {}

    public BaseResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Thành công
    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(200, "Success", data);
    }

    // Xử lý lỗi (Dùng cho Auth hoặc Validation)
    public static <T> BaseResponse<T> error(Integer code, String message) {
        return new BaseResponse<>(code, message, null);
    }

    // Getters and Setters...
}