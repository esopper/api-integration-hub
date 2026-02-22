package com.eriksopper.hub.mapper;

import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherLocationResponse;
import com.eriksopper.hub.web.dto.LocationDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocationMapper {
    public List<LocationDto> toDtos(List<OpenWeatherLocationResponse> externalLocations) {
        List<LocationDto> locations = new ArrayList<>();

        for(OpenWeatherLocationResponse location : externalLocations) {
            LocationDto locationDto = new LocationDto();

            locationDto.setName(location.getName());
            locationDto.setLat(location.getLat());
            locationDto.setLon(location.getLon());
            locationDto.setState(location.getState());
            locationDto.setCountry(location.getCountry());

            locations.add(locationDto);
        }

        return locations;
    }
}
