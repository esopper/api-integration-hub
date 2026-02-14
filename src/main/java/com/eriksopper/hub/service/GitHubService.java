package com.eriksopper.hub.service;

import com.eriksopper.hub.integrations.github.client.GitHubClient;
import com.eriksopper.hub.mapper.GitHubMapper;
import com.eriksopper.hub.web.dto.GitHubRepositoryDto;
import com.eriksopper.hub.web.dto.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GitHubService {
    private final GitHubClient client;
    private final GitHubMapper mapper;

    public PageResponse<GitHubRepositoryDto> search(String term, int pageIndex, int pageSize, String sort, String order) {
        int perPage = Math.max(1, Math.min(pageSize, 100));
        int page = Math.max(1, pageIndex + 1);
        return mapper.toPage(client.search(term, page, perPage, sort, order));
    }
}
