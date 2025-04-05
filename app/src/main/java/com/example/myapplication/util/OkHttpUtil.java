package com.example.myapplication.util;


import com.google.gson.Gson;

import okhttp3.OkHttpClient;

public class OkHttpUtil {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static OkHttpClient getClient() {
        return client;
    }

    public static Gson getGson() {
        return gson;
    }


}
