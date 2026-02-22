package com.eriksopper.hub.service;

import com.eriksopper.hub.integrations.openweather.client.OpenWeatherClient;
import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherResponse;
import com.eriksopper.hub.mapper.LocationMapper;
import com.eriksopper.hub.mapper.WeatherMapper;
import com.eriksopper.hub.web.dto.WeatherDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {
    @Test
    public void search_delegatesToClientAndMapper() {
        OpenWeatherClient client = mock(OpenWeatherClient.class);
        WeatherMapper weatherMapper = mock(WeatherMapper.class);
        LocationMapper locationMapper = mock(LocationMapper.class);
        WeatherService service = new WeatherService(client, weatherMapper, locationMapper);

        OpenWeatherResponse raw = new OpenWeatherResponse();
        WeatherDto mapped = new WeatherDto();

        when(client.fetchWeather(44.9, -93.1, "metric")).thenReturn(raw);
        when(weatherMapper.toDto(raw)).thenReturn(mapped);

        WeatherDto dto = service.getWeather(44.9, -93.1, "metric");

        verify(client).fetchWeather(44.9, -93.1, "metric");
        verify(weatherMapper).toDto(raw);
        assertThat(dto).isSameAs(mapped);
    }
}
