package com.effix.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    private ResponseDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Constructor without data
    public ResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(true, message, data);
    }

    public static ResponseDTO<?> success(String message) {
        return new ResponseDTO<>(true, message);
    }

    public static <T> ResponseDTO<T> success(T data) {
        return success("Success", data);
    }

    public static <T> ResponseDTO<T> failure(String message) {
        return new ResponseDTO<>(false, message, null);
    }

    public static <T> ResponseDTO<T> failure(String message, T data) {
        return new ResponseDTO<>(false, message, data);
    }

}
