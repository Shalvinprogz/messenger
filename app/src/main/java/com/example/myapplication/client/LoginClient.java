package com.example.myapplication.client;

import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.request.LoginRequest;
import com.example.myapplication.response.BaseResponse;
import com.example.myapplication.response.UserDTO;
import com.example.myapplication.util.OkHttpUtil;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.AccessDeniedException;

import lombok.Value;
import okhttp3.OkHttpClient;

public class LoginClient extends Client{
    public static UserDTO login(String username, String password) throws IOException {
        String url = BASE_URL + "login";
        LoginRequest request = new LoginRequest(username, password);
        String json = OkHttpUtil.getGson().toJson(request);
        String response = post(url, json);

        Type baseType = new TypeToken<BaseResponse<JsonElement>>() {}.getType();
        BaseResponse<JsonElement> baseResponse = OkHttpUtil.getGson().fromJson(response, baseType);

        if (baseResponse == null || baseResponse.getData() == null) {
            throw new IOException("Invalid response");
        }

        if (baseResponse.isStatus()) {
            return OkHttpUtil.getGson().fromJson(baseResponse.getData(), UserDTO.class);
        } else {
            String error = baseResponse.getData().isJsonPrimitive()
                    ? baseResponse.getData().getAsString()
                    : "Unknown error";
            throw new IOException(error);
        }
    }

}
