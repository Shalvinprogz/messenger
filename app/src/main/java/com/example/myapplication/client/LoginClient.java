package com.example.myapplication.client;

import com.example.myapplication.request.LoginRequest;
import com.example.myapplication.response.UserDTO;
import com.example.myapplication.util.OkHttpUtil;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import lombok.Value;
import okhttp3.OkHttpClient;

public class LoginClient extends Client{
    public static UserDTO login(String username, String password) throws IOException {
        String url = BASE_URL + "login";
        LoginRequest request = new LoginRequest(username, password);
        String json = OkHttpUtil.getGson().toJson(request);
        String response = post(url, json);
        UserDTO userDTO = OkHttpUtil.getGson().fromJson(response, UserDTO.class);
        if (userDTO == null) {
            return null;
        }
        System.out.println(userDTO);
        return userDTO;
    }
}
