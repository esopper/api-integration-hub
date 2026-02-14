package com.eriksopper.hub.mapper;

import com.eriksopper.hub.integrations.github.dto.GitHubRepositoryExternal;
import com.eriksopper.hub.integrations.github.dto.GitHubSearchResponse;
import com.eriksopper.hub.web.dto.GitHubRepositoryDto;
import com.eriksopper.hub.web.dto.PageResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GitHubMapper {
    public PageResponse<GitHubRepositoryDto> toPage(GitHubSearchResponse searchResponse) {
        if (searchResponse == null) {
            return new PageResponse<>(0, List.of());
        }

        int total = Math.min(searchResponse.getTotalCount(), 1000);
        List<GitHubRepositoryDto> items = (searchResponse.getItems() == null) ? List.of()
                : searchResponse.getItems().stream().map(this::toDto).toList();

        return new PageResponse<>(total, items);
    }

    public GitHubRepositoryDto toDto(GitHubRepositoryExternal externalRepo) {
        GitHubRepositoryDto webRepo = new GitHubRepositoryDto();
        webRepo.setName(externalRepo.getName());
        webRepo.setDescription(externalRepo.getDescription());
        webRepo.setHtmlUrl(externalRepo.getHtmlUrl());
        webRepo.setStars(externalRepo.getStargazersCount());

        return webRepo;
    }
}
