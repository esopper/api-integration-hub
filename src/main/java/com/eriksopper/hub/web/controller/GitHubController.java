package com.eriksopper.hub.web.controller;

import com.eriksopper.hub.service.GitHubService;
import com.eriksopper.hub.web.dto.ApiResponse;
import com.eriksopper.hub.web.dto.GitHubRepositoryDto;
import com.eriksopper.hub.web.dto.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
@AllArgsConstructor
public class GitHubController {
    private final GitHubService gitHubService;

    @GetMapping("/repos/search")
    public ResponseEntity<ApiResponse<PageResponse<GitHubRepositoryDto>>> search(
            @RequestParam(defaultValue = "") String term,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order
    ) {
        PageResponse<GitHubRepositoryDto> page = gitHubService.search(term, pageIndex, pageSize, sort, order);
        ApiResponse<PageResponse<GitHubRepositoryDto>> response = ApiResponse.ok(page);
        return ResponseEntity.ok(response);
    }
}
