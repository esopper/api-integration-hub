package com.eriksopper.hub.integrations.openweather.client;

import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherLocationResponse;
import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherResponse;
import com.eriksopper.hub.integrations.openweather.exception.OpenWeatherUpstreamException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
public class OpenWeatherHttpClient implements OpenWeatherClient{
    public static final int LOCATION_RESULT_LIMIT = 5;
    private final RestTemplate rest;
    private final String openWeatherApiKey;
    private final String baseUrl;

    public OpenWeatherHttpClient(
            RestTemplate rest,
            @Value("${openweather.api.key:}") String openWeatherApiKey,
            @Value("${openweather.base-url:https://api.openweathermap.org}") String baseUrl) {
        this.rest = rest;
        this.openWeatherApiKey = openWeatherApiKey;
        this.baseUrl = baseUrl;
    }

    @Override
    public OpenWeatherResponse fetchWeather(double lat, double lon, String units) {

        String url = UriComponentsBuilder.fromUriString(baseUrl + "/data/3.0/onecall")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("exclude", "minutely,hourly")
                .queryParam("units", units)
                .queryParam("appid", openWeatherApiKey)
                .toUriString();

        try {
            return rest.getForObject(url, OpenWeatherResponse.class);
        } catch (HttpStatusCodeException ex) {
            throw new OpenWeatherUpstreamException("OpenWeather error: " + ex.getResponseBodyAsString(), ex.getStatusCode().value());
        }
    }

    @Override
    public List<OpenWeatherLocationResponse> fetchLocations(String q) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/geo/1.0/direct")
                .queryParam("q", q.trim())
                .queryParam("limit", LOCATION_RESULT_LIMIT)
                .queryParam("appid", openWeatherApiKey)
                .encode()
                .build()
                .toUri();

        try {
            return rest.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<OpenWeatherLocationResponse>>() {}).getBody();
        } catch (HttpStatusCodeException ex) {
            throw new OpenWeatherUpstreamException("OpenWeather error: " + ex.getResponseBodyAsString(), ex.getStatusCode().value());
        }
    }
}
