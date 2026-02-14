package com.eriksopper.hub.web.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void ok_setsSuccessTrueAndData() {
        ApiResponse<String> r = ApiResponse.ok("x");
        assertThat(r.isSuccess()).isTrue();
        assertThat(r.getData()).isEqualTo("x");
        assertThat(r.getMessage()).isNull();
    }

    @Test
    void error_setsSuccessFalseAndMessage() {
        ApiResponse<Void> r = ApiResponse.error("bad");
        assertThat(r.isSuccess()).isFalse();
        assertThat(r.getMessage()).isEqualTo("bad");
        assertThat(r.getData()).isNull();
    }
}
