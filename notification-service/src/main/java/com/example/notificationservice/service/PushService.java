package com.example.notificationservice.service;

import org.springframework.stereotype.Service;

@Service
public class PushService {

    public void sendPush(Long userId, String message){
        System.out.println("Push sent");
    }
}