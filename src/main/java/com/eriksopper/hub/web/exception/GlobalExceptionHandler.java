package com.eriksopper.hub.web.exception;

import com.eriksopper.hub.domain.exception.ResourceNotFoundException;
import com.eriksopper.hub.integrations.github.exception.GitHubRateLimitException;
import com.eriksopper.hub.integrations.github.exception.GitHubUpstreamException;
import com.eriksopper.hub.integrations.openweather.exception.OpenWeatherUpstreamException;
import com.eriksopper.hub.web.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(GitHubRateLimitException.class)
    public ResponseEntity<ApiResponse<Void>> handleRateLimit(GitHubRateLimitException ex) {
        HttpHeaders headers = new HttpHeaders();
        if (ex.getRetryAfterSeconds() != null) {
            headers.set("Retry-After", String.valueOf(ex.getRetryAfterSeconds()));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(headers)
                .body(ApiResponse.error("Upstream rate limit: " + ex.getMessage()));
    }

    @ExceptionHandler(GitHubUpstreamException.class)
    public ResponseEntity<ApiResponse<Void>> handleGitHubUpstream(GitHubUpstreamException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(OpenWeatherUpstreamException.class)
    public ResponseEntity<ApiResponse<Void>> handleGitHubUpstream(OpenWeatherUpstreamException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + " " + e.getDefaultMessage())
                .findFirst().orElse("Validation failed");
        return ResponseEntity.badRequest().body(ApiResponse.error(msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Unexpected error"));
    }
}
