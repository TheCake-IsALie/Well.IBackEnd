package org.generation.wellibackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.SpotifyApi;

@Configuration
public class SpotifyAPIConfig {
    @Autowired
    SpotifyConfig spotifyConfig;

    @Bean
    public SpotifyApi spotifyApi(){
        return new SpotifyApi.Builder()
                .setClientId(spotifyConfig.clientId())
                .setClientSecret(spotifyConfig.clientSecret())
                .build();

    }
}
