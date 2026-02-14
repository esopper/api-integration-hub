package com.eriksopper.hub.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ApiResponse<T> {
    @Getter @Setter
    private boolean success;

    @Getter @Setter
    private String message;

    @Getter @Setter
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, null, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
