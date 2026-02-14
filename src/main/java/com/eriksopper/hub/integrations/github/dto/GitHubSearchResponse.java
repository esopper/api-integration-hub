package com.eriksopper.hub.integrations.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GitHubSearchResponse {
    @JsonProperty("total_count")
    private int totalCount;
    @JsonProperty("incomplete_results")
    private boolean incompleteResults;
    private List<GitHubRepositoryExternal> items;
}
