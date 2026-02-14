package com.eriksopper.hub.integrations.github.exception;

import lombok.Getter;

public class GitHubRateLimitException extends RuntimeException {
    @Getter
    private final Integer retryAfterSeconds;

    public GitHubRateLimitException(String message, Integer retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }
}
