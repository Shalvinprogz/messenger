package com.example.myapplication.util;


import com.google.gson.Gson;

import lombok.Getter;
import okhttp3.OkHttpClient;

@Getter
public class HttpUtil {
    private static volatile HttpUtil instance;
    private final OkHttpClient client;
    private final Gson gson;

    private HttpUtil() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public static HttpUtil getInstance() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }
}
