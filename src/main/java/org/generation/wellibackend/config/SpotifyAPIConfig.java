package org.generation.wellibackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.SpotifyApi;

@Configuration
public class SpotifyAPIConfig {

    private final SpotifyConfig spotifyConfig;

    public SpotifyAPIConfig(SpotifyConfig spotifyConfig) {
        this.spotifyConfig = spotifyConfig;
    }

    @Bean
    public SpotifyApi spotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(spotifyConfig.clientId())
                .setClientSecret(spotifyConfig.clientSecret())
                .build();
    }
}