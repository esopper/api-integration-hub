package com.eriksopper.hub.web.dto;

import java.util.List;

public record PageResponse<T>(int total, List<T> items) {}
