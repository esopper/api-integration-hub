package com.eriksopper.hub.mapper;

import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherResponse;
import com.eriksopper.hub.web.dto.WeatherDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherMapper {
    public WeatherDto toDto(OpenWeatherResponse externalWeather) {
        if (externalWeather == null) {
            return null;
        }

        WeatherDto webWeather = new WeatherDto();

        OpenWeatherResponse.Current current = externalWeather.getCurrent();
        if (current != null) {
            WeatherDto.Now now = new WeatherDto.Now();
            now.setTemp(current.getTemp());
            now.setFeelsLike(current.getFeelsLike());
            now.setDescription((current.getWeather() != null && !current.getWeather().isEmpty())
                    ? current.getWeather().get(0).getDescription() : "");
            now.setIcon((current.getWeather() != null && !current.getWeather().isEmpty())
                    ? current.getWeather().get(0).getIcon() : "");
            webWeather.setCurrent(now);
        }

        List<WeatherDto.Day> webDays = new ArrayList<>();
        if (externalWeather.getDaily() != null) {
            for (OpenWeatherResponse.Day externalDay : externalWeather.getDaily()) {
                WeatherDto.Day webDay = getDay(externalDay);
                webDays.add(webDay);
            }
        }
        webWeather.setForecast(webDays);

        return webWeather;
    }

    private static WeatherDto.Day getDay(OpenWeatherResponse.Day externalDay) {
        WeatherDto.Day webDay = new WeatherDto.Day();
        webDay.setDt(externalDay.getDt());
        if (externalDay.getTemp() != null) {
            webDay.setMax(externalDay.getTemp().getDay());
            webDay.setMin(externalDay.getTemp().getEve());
            webDay.setFeelsLikeMax(externalDay.getFeels_like().getDay());
            webDay.setFeelsLikeMin(externalDay.getFeels_like().getEve());
        }
        webDay.setDescription((externalDay.getWeather() != null && !externalDay.getWeather().isEmpty())
                ? externalDay.getWeather().get(0).getDescription() : "");
        webDay.setIcon((externalDay.getWeather() != null && !externalDay.getWeather().isEmpty())
                ? externalDay.getWeather().get(0).getIcon() : "");
        webDay.setSummary(externalDay.getSummary());
        return webDay;
    }
}
