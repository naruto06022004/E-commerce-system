package com.example.authservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Tự động tạo Builder pattern
@AllArgsConstructor // Bắt buộc phải có để @Builder hoạt động đúng
@NoArgsConstructor // Cần thiết để các thư viện như Jackson có thể map dữ liệu
public class LoginResponseDto {
    private String accessToken;  // Phải chính xác từng chữ cái
    private String refreshToken; // Phải chính xác từng chữ cái
}
