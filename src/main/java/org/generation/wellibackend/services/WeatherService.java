package org.generation.wellibackend.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class WeatherService {
    private final RestClient rest;

    public WeatherService(RestClient.Builder builder) {
        this.rest = builder.baseUrl("https://api.open-meteo.com").build();
    }

    public Map<String, Object> getForecast(double lat, double lon, String currentVars, String hourlyVars, String timezone) {
        try {
            return rest.get()
                    .uri(uri -> uri.path("/v1/forecast")
                            .queryParam("latitude", lat)
                            .queryParam("longitude", lon)
                            .queryParam("current", currentVars)
                            .queryParam("hourly", hourlyVars)
                            .queryParam("timezone", timezone)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            throw new IllegalStateException("Weather provider unavailable", ex);
        }
    }

}
