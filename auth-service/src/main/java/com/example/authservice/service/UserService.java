package com.example.authservice.service;


import com.example.authservice.request.LoginRequestDto;
import com.example.authservice.request.UserRegistrationDto;
import com.example.authservice.response.LoginResponseDto;

public interface UserService {
    void createUser(UserRegistrationDto userRegistrationDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}