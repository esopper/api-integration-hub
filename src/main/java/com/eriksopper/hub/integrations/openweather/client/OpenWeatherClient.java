package com.eriksopper.hub.integrations.openweather.client;

import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherLocationResponse;
import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherResponse;

import java.util.List;

public interface OpenWeatherClient {
    OpenWeatherResponse fetchWeather(double lat, double lon, String units);
    List<OpenWeatherLocationResponse> fetchLocations(String q);
}
