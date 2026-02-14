package com.eriksopper.hub.web.controller;

import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherResponse;
import com.eriksopper.hub.service.WeatherService;
import com.eriksopper.hub.web.dto.ApiResponse;
import com.eriksopper.hub.web.dto.WeatherDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@AllArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<ApiResponse<WeatherDto>> getWeather(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "metric") String units
    ) {
        WeatherDto weather = weatherService.getWeather(lat, lon, units);
        ApiResponse<WeatherDto> response = ApiResponse.ok(weather);
        return ResponseEntity.ok(response);
    }
}
