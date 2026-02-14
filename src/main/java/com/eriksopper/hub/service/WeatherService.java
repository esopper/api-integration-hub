package com.eriksopper.hub.service;

import com.eriksopper.hub.integrations.openweather.client.OpenWeatherClient;
import com.eriksopper.hub.mapper.WeatherMapper;
import com.eriksopper.hub.web.dto.WeatherDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeatherService {
    private final OpenWeatherClient client;
    private final WeatherMapper mapper;

    public WeatherDto getWeather(double lat, double lon, String units) {
        return mapper.toDto(client.fetch(lat, lon, units));
    }

//    private static class Location {
//        public Location() {
//
//        }
//
//        public float lat;
//        public float lon;
//    }
//
//    @Value("${openweather.api.key}")
//    private String openWeatherApiKey;
//
//    public OpenWeatherResponse fetchWeather(String city) {
//        Location location = fetchLocation(city);
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        String url = "https://api.openweathermap.org/data/3.0/onecall?lat=" + location.lat + "&lon=" + location.lon + "&units=imperial&exclude=minutely,hourly&appid=" + openWeatherApiKey;
//
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
//
//        try {
//            ResponseEntity<OpenWeatherResponse> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, OpenWeatherResponse.class);
//            return response.getBody();
//        }
//        catch (HttpClientErrorException.NotFound ex) {
//            throw new WeatherCityNotFoundException(city);
//        }
//    }
//
//    private Location fetchLocation(String city) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String url = "https://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + openWeatherApiKey;
//
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
//
//        try {
//            ResponseEntity<Location[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Location[].class);
//            return response.getBody()[0];
//        }
//        catch (HttpClientErrorException.NotFound ex) {
//            throw new WeatherCityNotFoundException(city);
//        }
//    }
}
