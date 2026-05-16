package com.example.authservice.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Tự động tạo Builder pattern
@AllArgsConstructor // Bắt buộc phải có để @Builder hoạt động đúng
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}
