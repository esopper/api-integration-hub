package com.eriksopper.hub.service;

import com.eriksopper.hub.integrations.openweather.client.OpenWeatherClient;
import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherResponse;
import com.eriksopper.hub.mapper.WeatherMapper;
import com.eriksopper.hub.web.dto.WeatherDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {
    @Test
    public void search_delegatesToClientAndMapper() {
        OpenWeatherClient client = mock(OpenWeatherClient.class);
        WeatherMapper mapper = mock(WeatherMapper.class);
        WeatherService service = new WeatherService(client, mapper);

        OpenWeatherResponse raw = new OpenWeatherResponse();
        WeatherDto mapped = new WeatherDto();

        when(client.fetch(44.9, -93.1, "metric")).thenReturn(raw);
        when(mapper.toDto(raw)).thenReturn(mapped);

        WeatherDto dto = service.getWeather(44.9, -93.1, "metric");

        verify(client).fetch(44.9, -93.1, "metric");
        verify(mapper).toDto(raw);
        assertThat(dto).isSameAs(mapped);
    }
}
