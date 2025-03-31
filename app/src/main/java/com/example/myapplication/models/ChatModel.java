package com.example.myapplication.models;

public class ChatModel {
    private final long id;
    private final String name;
    private final String lastMessage;
    private final String time;
    private int unreadCount;

    public ChatModel(long id, String name, String lastMessage, String time, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.unreadCount = unreadCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}