package com.eriksopper.hub.service;

import com.eriksopper.hub.integrations.github.client.GitHubClient;
import com.eriksopper.hub.integrations.github.dto.GitHubSearchResponse;
import com.eriksopper.hub.mapper.GitHubMapper;
import com.eriksopper.hub.web.dto.GitHubRepositoryDto;
import com.eriksopper.hub.web.dto.PageResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GitHubServiceTest {
    @Test
    void search_delegatesToClientAndMapper() {
        GitHubClient client = mock(GitHubClient.class);
        GitHubMapper mapper = spy(new GitHubMapper());
        GitHubService service = new GitHubService(client, mapper);

        GitHubSearchResponse raw = new GitHubSearchResponse();
        when(client.search("term", 3, 5, "stars", "desc")).thenReturn(raw);

        PageResponse<GitHubRepositoryDto> expected = new PageResponse<>(0, java.util.List.of());
        doReturn(expected).when(mapper).toPage(raw);

        PageResponse<GitHubRepositoryDto> page =
                service.search("term", 2, 5, "stars", "desc");

        // verify client called with exact params
        verify(client).search("term", 3, 5, "stars", "desc");
        // verify mapper used
        verify(mapper).toPage(raw);
        assertThat(page).isSameAs(expected);
    }
}
