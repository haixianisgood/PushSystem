package com.example.pushsystem.web.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response <T> {
    private int code;
    private String info;
    private T body;

    public static <T> Response<T> buildSuccess() {
        return new Response<>(200, "success", null);
    }

}
