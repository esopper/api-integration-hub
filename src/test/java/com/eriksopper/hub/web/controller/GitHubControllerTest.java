package com.eriksopper.hub.web.controller;

import com.eriksopper.hub.service.GitHubService;
import com.eriksopper.hub.web.dto.GitHubRepositoryDto;
import com.eriksopper.hub.web.dto.PageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GitHubControllerTest {
    @Test
    void search_returnsOkEnvelope() throws Exception {
        GitHubService service = mock(GitHubService.class);
        GitHubController controller = new GitHubController(service);

        PageResponse<GitHubRepositoryDto> page =
                new PageResponse<>(1, List.of(new GitHubRepositoryDto()));
        when(service.search("", 0, 10, null, null)).thenReturn(page);

        MockMvc mvc = standaloneSetup(controller).build();

        mvc.perform(get("/api/github/repos/search").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.total").value(1));
    }
}
