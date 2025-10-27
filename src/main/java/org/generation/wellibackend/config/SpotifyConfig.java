package org.generation.wellibackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spotify.api")
public record SpotifyConfig(String clientId, String clientSecret) {
}
