package com.example.myapplication.client;

import com.example.myapplication.util.HttpUtil;

import java.io.IOException;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Client {
    public static final String BASE_URL = "http://192.168.1.8:8888/messenger/";
    @SneakyThrows
    static String post(String url, String json) throws IOException {
        OkHttpClient client = HttpUtil.getInstance().getClient();
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                return null;
            }
        }
    }
    static String get(String url) throws IOException {
        OkHttpClient client = HttpUtil.getInstance().getClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                return null;
            }
        }
    }
}
