package com.friends.Entity.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse<T> {
    private int code;
    private T message;

    public static <T> ErrorResponse<T> of(int code, T message) {
        ErrorResponse<T> response = new ErrorResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}