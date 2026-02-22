package com.eriksopper.hub.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
    private String name;
    private String country;
    private String state;
    private String lat;
    private String lon;
}
