package org.generation.wellibackend.controllers;

import org.generation.wellibackend.services.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weather;

    public WeatherController(WeatherService weather) {
        this.weather = weather;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> get(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam(value = "current", defaultValue = "temperature_2m,wind_speed_10m") String currentVars,
            @RequestParam(value = "hourly", defaultValue = "temperature_2m,relative_humidity_2m,wind_speed_10m") String hourlyVars,
            @RequestParam(value = "timezone", defaultValue = "auto") String timezone
    ) {
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid latitude/longitude range"));
        }
        var out = weather.getForecast(lat, lon, currentVars, hourlyVars, timezone);
        return ResponseEntity.ok(out);
    }
}
