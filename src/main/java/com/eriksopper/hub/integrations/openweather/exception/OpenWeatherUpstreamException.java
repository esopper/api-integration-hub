package com.eriksopper.hub.integrations.openweather.exception;

import lombok.Getter;

public class OpenWeatherUpstreamException extends RuntimeException {
    @Getter
    private final int status;

    public OpenWeatherUpstreamException(String message, int status) {
        super(message);
        this.status = status;
    }
}
