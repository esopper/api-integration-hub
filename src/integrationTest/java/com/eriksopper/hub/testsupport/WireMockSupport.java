package com.eriksopper.hub.testsupport;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class WireMockSupport {
    private WireMockSupport() {}

    // ===== OpenWeather stubs =====
    public static MappingBuilder owGetOneCall(StringValuePattern lat, StringValuePattern lon) {
        return get(urlPathEqualTo("/data/3.0/onecall"))
                .withQueryParam("lat", lat)
                .withQueryParam("lon", lon);
    }

    public static String owHappyBody() {
        return """
      { "current": { "temp": 12.3, "feels_like": 11.0, "weather":[{"description":"cloudy"}] },
        "daily": [ { "temp": { "day": 13.0 }, "weather":[{"description":"rain"}] } ]
      }
    """;
    }

    // ===== GitHub stubs =====
    public static MappingBuilder ghSearch() {
        return get(urlPathEqualTo("/search/repositories"));
    }

    public static String ghHappyBody() {
        return """
      {
        "total_count": 2,
        "items": [
          { "id": 1, "full_name": "octo/one", "stargazers_count": 10, "html_url": "https://github.com/octo/one" },
          { "id": 2, "full_name": "octo/two", "stargazers_count": 20, "html_url": "https://github.com/octo/two" }
        ]
      }
    """;
    }

    // ===== Common responses =====
    public static ResponseDefinitionBuilder okJson(String body) {
        return aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(body);
    }

    public static ResponseDefinitionBuilder delayedOkJson(String body, int ms) {
        return okJson(body).withFixedDelay(ms);
    }

    public static ResponseDefinitionBuilder upstreamError(int status, String msg) {
        return aResponse().withStatus(status)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"message\":\"" + msg + "\"}");
    }
}
