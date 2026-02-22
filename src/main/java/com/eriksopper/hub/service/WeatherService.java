package com.eriksopper.hub.service;

import com.eriksopper.hub.integrations.openweather.client.OpenWeatherClient;
import com.eriksopper.hub.mapper.LocationMapper;
import com.eriksopper.hub.mapper.WeatherMapper;
import com.eriksopper.hub.web.dto.LocationDto;
import com.eriksopper.hub.web.dto.WeatherDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WeatherService {
    private final OpenWeatherClient client;
    private final WeatherMapper weatherMapper;
    private final LocationMapper locationMapper;

    public WeatherDto getWeather(double lat, double lon, String units) {
        return weatherMapper.toDto(client.fetchWeather(lat, lon, units));
    }

    public List<LocationDto> getLocations(String q) {
        return locationMapper.toDtos(client.fetchLocations(q));
    }
}
