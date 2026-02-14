package com.eriksopper.hub.web.exception;

import com.eriksopper.hub.domain.exception.ResourceNotFoundException;
import com.eriksopper.hub.integrations.github.exception.GitHubRateLimitException;
import com.eriksopper.hub.integrations.github.exception.GitHubUpstreamException;
import com.eriksopper.hub.integrations.openweather.exception.OpenWeatherUpstreamException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class GlobalExceptionHandlerTest {
    @RestController
    @RequestMapping("/throw")
    @Validated
    static class ThrowingController {
        @GetMapping("/notfound")
        public void notFound() { throw new ResourceNotFoundException("x not found"); }

        @GetMapping("/rate")
        public void rate() { throw new GitHubRateLimitException("rate", 12); }

        @GetMapping("/gh")
        public void gh() { throw new GitHubUpstreamException("gh err", 502); }

        @GetMapping("/ow")
        public void ow() { throw new OpenWeatherUpstreamException("ow err", 503); }

        @GetMapping("/generic")
        public void generic() { throw new RuntimeException("boom"); }

        @PostMapping(value="/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
        public void validate(@RequestBody @Valid Payload payload) { /* never reached */ }
    }

    static class Payload {
        @NotBlank
        public String name;
    }

    private MockMvc mvc() {
        return standaloneSetup(new ThrowingController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void resourceNotFound_maps404() throws Exception {
        mvc().perform(get("/throw/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("x not found"));
    }

    @Test
    void rateLimit_maps429_andRetryAfterHeaderIfPresent() throws Exception {
        mvc().perform(get("/throw/rate"))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().string("Retry-After", "12"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void githubUpstream_propagatesStatus() throws Exception {
        mvc().perform(get("/throw/gh"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.message").value("gh err"));
    }

    @Test
    void openWeatherUpstream_propagatesStatus() throws Exception {
        mvc().perform(get("/throw/ow"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("ow err"));
    }

    @Test
    void validation_maps400_withFirstFieldMessage() throws Exception {
        String json = "{\"name\": \"\"}";
        mvc().perform(post("/throw/validate").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void generic_maps500_withGenericMessage() throws Exception {
        mvc().perform(get("/throw/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}
