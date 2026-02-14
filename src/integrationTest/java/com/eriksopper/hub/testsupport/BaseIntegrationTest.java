package com.eriksopper.hub.testsupport;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    protected static WireMockServer stub = new WireMockServer(options().dynamicPort());

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry r) {
        // Point clients at WireMock’s port instead of real APIs
        r.add("openweather.base-url", () -> "http://localhost:" + stub.port());
        r.add("openweather.api.key", () -> "test-api-key");
        r.add("github.base-url", () -> "http://localhost:" + stub.port());

        // Fast timeouts so timeout tests don’t hang
        r.add("http.client.connect-timeout-ms", () -> 500);
        r.add("http.client.read-timeout-ms", () -> 500);
    }

    @BeforeAll
    static void beforeAll() { stub.start(); }

    @AfterAll
    static void afterAll() { stub.stop(); }

    @Autowired
    protected TestRestTemplate rest;
}
