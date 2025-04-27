package com.example.myapplication.models;

import com.example.myapplication.enums.SocketMessageType;

import java.util.Date;

import lombok.Data;

@Data
public class SocketMessageDTO {
    private SocketMessageType messageType;
    private Object message;
    private Date timestamp;
    private String username;
    private Long convId;
    private String toUser;

    public SocketMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(SocketMessageType messageType) {
        this.messageType = messageType;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getConvId() {
        return convId;
    }

    public void setConvId(Long convId) {
        this.convId = convId;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
}
