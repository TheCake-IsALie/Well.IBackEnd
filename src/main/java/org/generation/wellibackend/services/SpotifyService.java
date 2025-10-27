package org.generation.wellibackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SpotifyService {
    @Autowired
    SpotifyApi spotifyApi;

    private void authenticate() {
        try {
            ClientCredentialsRequest credentialsRequest = spotifyApi.clientCredentials().build();
            ClientCredentials credentials = credentialsRequest.execute();
            spotifyApi.setAccessToken(credentials.getAccessToken());
        } catch (Exception e) {
            // Gestisci l'eccezione (es. loggala)
            throw new RuntimeException("Autenticazione Spotify fallita!", e);
        }
    }

    public List<PlaylistSimplified> searchPlaylists(String query) {
        try {
            // 1. Autentica (o assicurati che il token sia valido)
            // Per semplicità, autentichiamo ad ogni chiamata.
            authenticate();

            // 2. Costruisci la richiesta di ricerca
            SearchPlaylistsRequest searchRequest = spotifyApi.searchPlaylists(query)
                    .limit(10) // Vogliamo i primi 10 risultati
                    .build();

            // 3. Esegui la chiamata
            Paging<PlaylistSimplified> playlistPaging = searchRequest.execute();

            // 4. Restituisci la lista
            return Arrays.asList(playlistPaging.getItems());

        } catch (Exception e) {
            // Gestisci le eccezioni API (IOException, SpotifyWebApiException, ParseException)
            System.err.println("Errore durante la ricerca della playlist: " + e.getMessage());
            return Collections.emptyList(); // Restituisci una lista vuota in caso di errore
        }
    }
}
