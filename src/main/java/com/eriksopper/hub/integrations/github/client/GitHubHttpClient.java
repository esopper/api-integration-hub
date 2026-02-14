package com.eriksopper.hub.integrations.github.client;

import com.eriksopper.hub.integrations.github.dto.GitHubSearchResponse;
import com.eriksopper.hub.integrations.github.exception.GitHubRateLimitException;
import com.eriksopper.hub.integrations.github.exception.GitHubUpstreamException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;


@Component
public class GitHubHttpClient implements GitHubClient {
    private final RestTemplate rest;
    private final String token;
    private final String baseUrl;

    public GitHubHttpClient(
            RestTemplate rest,
            @Value("${github.token:}") String token,
            @Value("${github.base-url:https://api.github.com}") String baseUrl) {
        this.rest = rest;
        this.token = token;
        this.baseUrl = baseUrl;
    }

    @Override
    public GitHubSearchResponse search(String term, int page, int perPage, String sort, String order) {
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/search/repositories")
            .queryParam("q", (term == null || term.isBlank()) ? "stars:>0" : term.trim())
            .queryParam("per_page", Math.max(1, Math.min(perPage, 100)))
            .queryParam("page", Math.max(1, page))
            .queryParamIfPresent("sort", (sort == null || sort.isBlank()) ? Optional.empty() : Optional.of(sort))
            .queryParamIfPresent("order", (order == null || order.isBlank()) ? Optional.empty() : Optional.of(order))
            .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/vnd.github+json");
        headers.set("User-Agent", "eriksopper-hub/1.0");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        try {
            ResponseEntity<GitHubSearchResponse> res = rest.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), GitHubSearchResponse.class);
            return res.getBody();
        } catch (HttpClientErrorException.Forbidden ex) {
            Integer retry = ex.getResponseHeaders() != null
                    ? (ex.getResponseHeaders().getFirst("Retry-After") != null
                        ? Integer.valueOf(ex.getResponseHeaders().getFirst("Retry-After"))
                        : null)
                    : null;
            throw new GitHubRateLimitException("GitHub rate limit hit, try again in ", retry);
        } catch (HttpStatusCodeException ex) {
            throw new GitHubUpstreamException("GitHub error: " + ex.getResponseBodyAsString(), ex.getStatusCode().value());
        }
    }
}
