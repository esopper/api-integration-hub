package com.eriksopper.hub.integrations.openweather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenWeatherResponse {
    private Current current;
    private List<Day> daily;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Current {
        private double temp;
        @JsonProperty("feels_like")
        private double feelsLike;
        private List<Weather> weather;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Weather {
        private String description;
        private String icon;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Day {
        private long dt;
        private Temp temp;
        private Temp feels_like;
        private List<Weather> weather;
        private String summary;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Temp {
        private double day;
        private double eve;
    }
}
