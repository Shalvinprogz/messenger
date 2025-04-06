package com.example.myapplication.client;

public class MessageClient {
    private static MessageClient instance;
    MessageClient() {

    }
    public static MessageClient getInstance() {
        if (instance == null) {
            synchronized (MessageClient.class) {
                if (instance == null) {
                    instance = new MessageClient();
                }
            }
        }
        return instance;
    }

    public void sendMessage(String senderId, String receiverId, String message) {

    }

    public void receiveMessage(String receiverId) {

    }

    public void getHomePageMessages(String senderId, String receiverId) {

    }


}
