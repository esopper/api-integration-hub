package com.eriksopper.hub.integrations.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class GitHubRepositoryExternal {
    @Getter @Setter
    private String name;

    @Getter @Setter
    private String description;

    @Getter @Setter
    @JsonProperty("html_url")
    private String htmlUrl;

    @Getter @Setter
    @JsonProperty("stargazers_count")
    private int stargazersCount;
}
