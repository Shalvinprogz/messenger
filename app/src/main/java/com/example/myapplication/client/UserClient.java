package com.example.myapplication.client;

public class UserClient extends Client {

    private static UserClient instance;
    UserClient() {

    }
    public static UserClient getInstance() {
        if (instance == null) {
            synchronized (UserClient.class) {
                if (instance == null) {
                    instance = new UserClient();
                }
            }
        }
        return instance;
    }

    public void getAllMessages(String userId) {

    }
}
