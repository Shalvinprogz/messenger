package com.example.myapplication.client;

import com.example.myapplication.request.LoginRequest;
import com.example.myapplication.response.BaseResponse;
import com.example.myapplication.response.UserDTO;
import com.example.myapplication.util.HttpUtil;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

public class LoginClient extends Client{
    public static UserDTO login(String username, String password) throws IOException {
        String url = BASE_URL + "login";
        LoginRequest request = new LoginRequest(username, password);
        String json = HttpUtil.getInstance().getGson().toJson(request);
        String response = post(url, json);

        Type baseType = new TypeToken<BaseResponse<JsonElement>>() {}.getType();
        BaseResponse<JsonElement> baseResponse = HttpUtil.getInstance().getGson().fromJson(response, baseType);

        if (baseResponse == null || baseResponse.getData() == null) {
            throw new IOException("Invalid response");
        }

        if (baseResponse.isStatus()) {
            return HttpUtil.getInstance().getGson().fromJson(baseResponse.getData(), UserDTO.class);
        } else {
            String error = baseResponse.getData().isJsonPrimitive()
                    ? baseResponse.getData().getAsString()
                    : "Unknown error";
            throw new IOException(error);
        }
    }

}
