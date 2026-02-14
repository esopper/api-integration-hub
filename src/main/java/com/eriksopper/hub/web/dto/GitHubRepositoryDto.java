package com.eriksopper.hub.web.dto;

import lombok.Getter;
import lombok.Setter;

public class GitHubRepositoryDto {
    @Getter @Setter
    private String name;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private String htmlUrl;

    @Getter @Setter
    private int stars;

    @Getter @Setter
    private String language;
}
