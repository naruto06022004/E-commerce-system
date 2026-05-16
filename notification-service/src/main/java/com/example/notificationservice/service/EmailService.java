package com.example.notificationservice.service;

import com.example.notificationservice.dto.EmailBatchEvent;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendBatch(EmailBatchEvent event){

        for(String email : event.getEmails()){
            System.out.println("Sending email to " + email);
        }
    }
}