package com.eriksopper.hub.integrations.github.client;

import com.eriksopper.hub.integrations.github.dto.GitHubSearchResponse;
import com.eriksopper.hub.integrations.github.exception.GitHubRateLimitException;
import com.eriksopper.hub.integrations.github.exception.GitHubUpstreamException;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class GitHubHttpClientTest {

    @Test
    void search_buildsUrl_setsAuth_andMapsResponse() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        GitHubHttpClient client = new GitHubHttpClient(rest, "t0k3n", "https://api.github.com");

        server.expect(ExpectedCount.once(),
                        requestTo(org.hamcrest.Matchers.allOf(
                                org.hamcrest.Matchers.containsString("/search/repositories"),
                                org.hamcrest.Matchers.containsString("q=term"),
                                org.hamcrest.Matchers.containsString("page=2"),
                                org.hamcrest.Matchers.containsString("per_page=5"),
                                org.hamcrest.Matchers.containsString("sort=stars"),
                                org.hamcrest.Matchers.containsString("order=desc")
                        )))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer t0k3n"))
                .andRespond(withSuccess(
                        "{\"total_count\":1,\"incomplete_results\":false,\"items\":[]}",
                        MediaType.APPLICATION_JSON));

        GitHubSearchResponse resp = client.search("term", 2, 5, "stars", "desc");
        assertThat(resp.getTotalCount()).isEqualTo(1);

        server.verify();
    }

    @Test
    void search_mapsForbiddenToRateLimit_withRetryAfter() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        GitHubHttpClient client = new GitHubHttpClient(rest, "t0k3n", "https://api.github.com");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Retry-After", "12");

        server.expect(requestTo(org.hamcrest.Matchers.containsString("/search/repositories")))
                .andRespond(withStatus(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\":\"API rate limit exceeded\"}")
                        .headers(responseHeaders));

        assertThatThrownBy(() -> client.search("term", 1, 10, null, null))
                .isInstanceOf(GitHubRateLimitException.class);

        server.verify();
    }

    @Test
    void search_maps5xxToUpstream_withStatus() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        GitHubHttpClient client = new GitHubHttpClient(rest, null, "https://api.github.com");

        server.expect(requestTo(org.hamcrest.Matchers.containsString("/search/repositories")))
                .andRespond(withStatus(HttpStatus.BAD_GATEWAY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\":\"upstream down\"}"));

        assertThatThrownBy(() -> client.search("x", 0, 10, null, null))
                .isInstanceOf(GitHubUpstreamException.class);

        server.verify();
    }
}
