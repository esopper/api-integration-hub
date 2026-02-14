package com.eriksopper.hub.integrations.github.client;

import com.eriksopper.hub.integrations.github.dto.GitHubSearchResponse;

public interface GitHubClient {
    GitHubSearchResponse search(String term, int page, int perPage, String sort, String order);
}
