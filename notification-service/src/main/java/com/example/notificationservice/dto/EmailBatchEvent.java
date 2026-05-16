package com.example.notificationservice.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailBatchEvent {

    private List<String> emails;
    private String title;
    private String message;
}