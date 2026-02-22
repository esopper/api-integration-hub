package com.eriksopper.hub.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherDto {
    private Now current;
    private List<Day> forecast;

    @Getter
    @Setter
    public static class Now {
        private double temp;
        private double feelsLike;
        private String description;
        private String icon;
    }

    @Getter
    @Setter
    public static class Day {
        private long dt;
        private double eve;
        private double day;
        private double feelsLikeEve;
        private double feelsLikeDay;
        private String description;
        private String icon;
        private String summary;
    }
}
