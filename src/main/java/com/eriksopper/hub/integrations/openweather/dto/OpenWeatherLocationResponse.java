package com.eriksopper.hub.integrations.openweather.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenWeatherLocationResponse {
    private String name;
    private String lat;
    private String lon;
    private String country;
    private String state;
}
