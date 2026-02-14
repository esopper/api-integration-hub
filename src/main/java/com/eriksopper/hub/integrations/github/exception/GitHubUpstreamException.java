package com.eriksopper.hub.integrations.github.exception;

import lombok.Getter;

public class GitHubUpstreamException extends RuntimeException {
    @Getter
    private final int status;

    public GitHubUpstreamException(String message, int status) {
        super(message);
        this.status = status;
    }
}
