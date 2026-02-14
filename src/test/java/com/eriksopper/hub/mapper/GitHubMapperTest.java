package com.eriksopper.hub.mapper;

import com.eriksopper.hub.integrations.github.dto.GitHubRepositoryExternal;
import com.eriksopper.hub.integrations.github.dto.GitHubSearchResponse;
import com.eriksopper.hub.web.dto.GitHubRepositoryDto;
import com.eriksopper.hub.web.dto.PageResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GitHubMapperTest {
    private final GitHubMapper mapper = new GitHubMapper();

    @Test
    public void toPage_nullResponse_returnsEmptyPage() {
        PageResponse<GitHubRepositoryDto> page = mapper.toPage(null);
        assertThat(page.total()).isEqualTo(0);
        assertThat(page.items()).isEmpty();
    }

    @Test
    public void toPage_mapsItemsAndTotal() {
        GitHubRepositoryExternal ext = new GitHubRepositoryExternal();
        ext.setName("repo");
        ext.setDescription("desc");
        ext.setHtmlUrl("https://x");
        ext.setStargazersCount(42);

        GitHubSearchResponse resp = new GitHubSearchResponse();
        resp.setTotalCount(123);
        resp.setItems(List.of(ext));

        PageResponse<GitHubRepositoryDto> page = mapper.toPage(resp);
        assertThat(page.total()).isEqualTo(123);
        assertThat(page.items()).hasSize(1);
        GitHubRepositoryDto dto = page.items().get(0);
        assertThat(dto.getName()).isEqualTo("repo");
        assertThat(dto.getDescription()).isEqualTo("desc");
        assertThat(dto.getHtmlUrl()).isEqualTo("https://x");
        assertThat(dto.getStars()).isEqualTo(42);
    }

    @Test
    public void toPage_mapsNullItemsToEmptyList() {
        GitHubSearchResponse resp = new GitHubSearchResponse();
        resp.setTotalCount(123);

        PageResponse<GitHubRepositoryDto> page = mapper.toPage(resp);
        assertThat(page.items()).isEmpty();
    }

    @Test
    public void toDto_mapsAllFields() {
        GitHubRepositoryExternal ext = new GitHubRepositoryExternal();
        ext.setName("r");
        ext.setDescription("d");
        ext.setHtmlUrl("u");
        ext.setStargazersCount(7);

        GitHubRepositoryDto dto = mapper.toDto(ext);
        assertThat(dto.getName()).isEqualTo("r");
        assertThat(dto.getDescription()).isEqualTo("d");
        assertThat(dto.getHtmlUrl()).isEqualTo("u");
        assertThat(dto.getStars()).isEqualTo(7);
    }
}
