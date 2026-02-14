package com.eriksopper.hub.integrations.openweather.client;

import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherResponse;

public interface OpenWeatherClient {
    OpenWeatherResponse fetch(double lat, double lon, String units);
}
