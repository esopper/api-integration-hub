package com.eriksopper.hub.integrations.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GitHubRepositoryExternal {
    private String name;

    private String description;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("stargazers_count")
    private int stargazersCount;
}
