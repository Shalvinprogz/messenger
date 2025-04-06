package com.example.myapplication.client;

import com.example.myapplication.models.HomeChatModel;
import com.example.myapplication.response.BaseResponse;
import com.example.myapplication.response.UserDTO;
import com.example.myapplication.util.HttpUtil;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;

public class MessageClient extends Client {
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

    public List<HomeChatModel> getHomePageMessages(String username) throws IOException {
        String url = BASE_URL + "user/homepage/" + username;
        String response = get(url);

        Type baseType = new TypeToken<BaseResponse<JsonElement>>() {}.getType();
        BaseResponse<JsonElement> baseResponse = HttpUtil.getInstance().getGson().fromJson(response, baseType);

        if (baseResponse == null || baseResponse.getData() == null) {
            throw new IOException("Invalid response");
        }

        if (baseResponse.isStatus()) {
            Type type = new TypeToken<List<HomeChatModel>>() {}.getType();
            return HttpUtil.getInstance().getGson().fromJson(baseResponse.getData(), type);
        } else {
            String error = baseResponse.getData().isJsonPrimitive()
                    ? baseResponse.getData().getAsString()
                    : "Unknown error";
            throw new IOException(error);
        }
    }


}
