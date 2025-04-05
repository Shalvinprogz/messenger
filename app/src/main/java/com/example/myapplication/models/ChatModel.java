package com.example.myapplication.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatModel {
    private String username;
    private final String name;
    private final String lastMessage;
    private final String time;
    private int unreadCount;

}