package com.eriksopper.hub.mapper;

import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherResponse;
import com.eriksopper.hub.web.dto.WeatherDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherMapperTest {
    private final WeatherMapper mapper = new WeatherMapper();

    @Test
    void toDto_null_returnsNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    void toDto_mapsCurrentAndForecast() {
        OpenWeatherResponse.Weather currentWeather = new OpenWeatherResponse.Weather();
        currentWeather.setDescription("cloudy");
        currentWeather.setIcon("currentIcon.jpg");

        OpenWeatherResponse.Current current = new OpenWeatherResponse.Current();
        current.setWeather(List.of(currentWeather));
        current.setTemp(12.3);
        current.setFeelsLike(11.0);

        OpenWeatherResponse.Temp dayTemp = new OpenWeatherResponse.Temp();
        dayTemp.setDay(34.1);
        dayTemp.setEve(31.3);

        OpenWeatherResponse.Temp dayFeelsLike = new OpenWeatherResponse.Temp();
        dayFeelsLike.setDay(29.6);
        dayFeelsLike.setEve(21.9);

        OpenWeatherResponse.Weather dayWeather = new OpenWeatherResponse.Weather();
        dayWeather.setDescription("sunny");
        dayWeather.setIcon("dayIcon.jpg");

        OpenWeatherResponse.Day day = new OpenWeatherResponse.Day();
        day.setDt(54321);
        day.setSummary("summary");
        day.setWeather(List.of(dayWeather));
        day.setTemp(dayTemp);
        day.setFeels_like(dayFeelsLike);

        OpenWeatherResponse ext = new OpenWeatherResponse();
        ext.setCurrent(current);
        ext.setDaily(List.of(day));

        WeatherDto dto = mapper.toDto(ext);

        assertThat(dto).isNotNull();

        assertThat(dto.getCurrent()).isNotNull();
        assertThat(dto.getCurrent().getTemp()).isEqualTo(12.3);
        assertThat(dto.getCurrent().getFeelsLike()).isEqualTo(11.0);
        assertThat(dto.getCurrent().getDescription()).isEqualTo("cloudy");
        assertThat(dto.getCurrent().getIcon()).isEqualTo("currentIcon.jpg");

        assertThat(dto.getForecast()).isNotNull();
        assertThat(dto.getForecast().get(0).getDt()).isEqualTo(54321);
        assertThat(dto.getForecast().get(0).getMax()).isEqualTo(34.1);
        assertThat(dto.getForecast().get(0).getMin()).isEqualTo(31.3);
        assertThat(dto.getForecast().get(0).getFeelsLikeMax()).isEqualTo(29.6);
        assertThat(dto.getForecast().get(0).getFeelsLikeMin()).isEqualTo(21.9);
        assertThat(dto.getForecast().get(0).getDescription()).isEqualTo("sunny");
        assertThat(dto.getForecast().get(0).getIcon()).isEqualTo("dayIcon.jpg");
        assertThat(dto.getForecast().get(0).getSummary()).isEqualTo("summary");
    }

    @Test
    void toDto_mapsNullCurrentAndForecast() {
        OpenWeatherResponse ext = new OpenWeatherResponse();

        WeatherDto dto = mapper.toDto(ext);

        assertThat(dto).isNotNull();
        assertThat(dto.getCurrent()).isNull();
        assertThat(dto.getForecast()).isEmpty();
    }

    @Test
    void toDto_mapsNullCurrentWeather() {
        OpenWeatherResponse.Current current = new OpenWeatherResponse.Current();

        OpenWeatherResponse ext = new OpenWeatherResponse();
        ext.setCurrent(current);

        WeatherDto dto = mapper.toDto(ext);

        assertThat(dto).isNotNull();
        assertThat(dto.getCurrent()).isNotNull();
        assertThat(dto.getCurrent().getDescription()).isEmpty();
        assertThat(dto.getCurrent().getIcon()).isEmpty();
    }

    @Test
    void toDto_mapsEmptyCurrentWeather() {
        OpenWeatherResponse.Current current = new OpenWeatherResponse.Current();
        current.setWeather(List.of());

        OpenWeatherResponse ext = new OpenWeatherResponse();
        ext.setCurrent(current);

        WeatherDto dto = mapper.toDto(ext);

        assertThat(dto).isNotNull();
        assertThat(dto.getCurrent()).isNotNull();
        assertThat(dto.getCurrent().getDescription()).isEmpty();
        assertThat(dto.getCurrent().getIcon()).isEmpty();
    }

    @Test
    void toDto_mapsNullDayTemp() {
        OpenWeatherResponse.Day day = new OpenWeatherResponse.Day();

        OpenWeatherResponse ext = new OpenWeatherResponse();
        ext.setDaily(List.of(day));

        WeatherDto dto = mapper.toDto(ext);

        assertThat(dto).isNotNull();
        assertThat(dto.getForecast()).isNotEmpty();
        assertThat(dto.getForecast().get(0).getMin()).isEqualTo(0);
        assertThat(dto.getForecast().get(0).getMax()).isEqualTo(0);
        assertThat(dto.getForecast().get(0).getFeelsLikeMin()).isEqualTo(0);
        assertThat(dto.getForecast().get(0).getFeelsLikeMax()).isEqualTo(0);
    }

    @Test
    void toDto_mapsNullDayWeather() {
        OpenWeatherResponse.Day day = new OpenWeatherResponse.Day();

        OpenWeatherResponse ext = new OpenWeatherResponse();
        ext.setDaily(List.of(day));

        WeatherDto dto = mapper.toDto(ext);

        assertThat(dto).isNotNull();
        assertThat(dto.getForecast()).isNotEmpty();
        assertThat(dto.getForecast().get(0).getDescription()).isEmpty();
        assertThat(dto.getForecast().get(0).getIcon()).isEmpty();
    }

    @Test
    void toDto_mapsEmptyDayWeather() {
        OpenWeatherResponse.Day day = new OpenWeatherResponse.Day();
        day.setWeather(List.of());

        OpenWeatherResponse ext = new OpenWeatherResponse();
        ext.setDaily(List.of(day));

        WeatherDto dto = mapper.toDto(ext);

        assertThat(dto).isNotNull();
        assertThat(dto.getForecast()).isNotEmpty();
        assertThat(dto.getForecast().get(0).getDescription()).isEmpty();
        assertThat(dto.getForecast().get(0).getIcon()).isEmpty();
    }
}
