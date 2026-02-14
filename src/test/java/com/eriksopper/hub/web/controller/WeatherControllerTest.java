package com.eriksopper.hub.web.controller;

import com.eriksopper.hub.service.WeatherService;
import com.eriksopper.hub.web.dto.ApiResponse;
import com.eriksopper.hub.web.dto.WeatherDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WeatherControllerTest {

    @Test
    void current_returnsOkEnvelopeWithWeather() throws Exception {
        WeatherService service = mock(WeatherService.class);
        WeatherController controller = new WeatherController(service);

        WeatherDto dto = new WeatherDto();
        WeatherDto.Now now = new WeatherDto.Now();
        now.setTemp(12.3);
        now.setFeelsLike(11.0);
        dto.setCurrent(now);
        dto.setForecast(Collections.emptyList());

        when(service.getWeather(44.98, -93.26, "metric")).thenReturn(dto);

        MockMvc mvc = standaloneSetup(controller).build();

        mvc.perform(get("/api/weather")
                        .param("lat", "44.98")
                        .param("lon", "-93.26")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))     // NOTE: after you fix ApiResponse.error to success=false for errors, this remains true for OK.
                .andExpect(jsonPath("$.data.current.temp").value(12.3))
                .andExpect(jsonPath("$.data.forecast").isArray());

        verify(service).getWeather(44.98, -93.26, "metric");
        verifyNoMoreInteractions(service);
    }
}
