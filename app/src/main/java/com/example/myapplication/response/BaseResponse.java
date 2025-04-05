package com.example.myapplication.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean status;
    private T data;

    public T getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }
}
