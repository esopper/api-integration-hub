package com.eriksopper.hub.testsupport;

import java.net.URI;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.boot.test.web.client.TestRestTemplate;

public final class CorsSupport {
    private CorsSupport() {}

    public static ResponseEntity<String> preflight(TestRestTemplate rest, String path,
                                                   String origin, HttpMethod method, String reqHeadersCsv) {
        HttpHeaders h = new HttpHeaders();
        h.setOrigin(origin);
        h.add(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, method.name());
        if (reqHeadersCsv != null && !reqHeadersCsv.isBlank()) {
            h.add(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, reqHeadersCsv);
        }
        var req = new HttpEntity<>(h);
        URI uri = UriComponentsBuilder.fromPath(path).build().toUri();
        return rest.exchange(uri, HttpMethod.OPTIONS, req, String.class);
    }

    public static ResponseEntity<String> getWithOrigin(TestRestTemplate rest, String path, String origin) {
        HttpHeaders h = new HttpHeaders();
        h.setOrigin(origin);
        var req = new HttpEntity<>(h);
        return rest.exchange(path, HttpMethod.GET, req, String.class);
    }
}
