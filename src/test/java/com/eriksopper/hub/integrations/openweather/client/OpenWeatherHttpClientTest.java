package com.eriksopper.hub.integrations.openweather.client;

import com.eriksopper.hub.integrations.openweather.dto.OpenWeatherResponse;
import com.eriksopper.hub.integrations.openweather.exception.OpenWeatherUpstreamException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class OpenWeatherHttpClientTest {

    @Test
    void fetch_buildsUrl_andMapsResponse() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        OpenWeatherHttpClient client = new OpenWeatherHttpClient(rest, "key123", "https://api.openweathermap.org");

        server.expect(requestTo(org.hamcrest.Matchers.allOf(
                        org.hamcrest.Matchers.containsString("/data/3.0/onecall"),
                        org.hamcrest.Matchers.containsString("lat=44.9"),
                        org.hamcrest.Matchers.containsString("lon=-93.1"),
                        org.hamcrest.Matchers.containsString("exclude=minutely,hourly"),
                        org.hamcrest.Matchers.containsString("units=metric"),
                        org.hamcrest.Matchers.containsString("appid=key123")
                )))
                .andRespond(withSuccess(
                        "{\"current\":{\"temp\":12.3},\"daily\":[]}",
                        MediaType.APPLICATION_JSON));

        OpenWeatherResponse res = client.fetch(44.9, -93.1, "metric");
        assertThat(res.getCurrent().getTemp()).isEqualTo(12.3);

        server.verify();
    }

    @Test
    void fetch_mapsErrorToOpenWeatherUpstreamException() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        OpenWeatherHttpClient client = new OpenWeatherHttpClient(rest, "key123", "https://api.openweathermap.org");

        server.expect(requestTo(org.hamcrest.Matchers.containsString("/data/3.0/onecall")))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"cod\":503,\"message\":\"service down\"}"));

        assertThatThrownBy(() -> client.fetch(0, 0, "metric"))
                .isInstanceOf(OpenWeatherUpstreamException.class)
                .hasMessageContaining("OpenWeather");

        server.verify();
    }
}
