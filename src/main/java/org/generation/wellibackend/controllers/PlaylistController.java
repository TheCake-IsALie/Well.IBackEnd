package org.generation.wellibackend.controllers;

import org.generation.wellibackend.services.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importato
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.List;

@RestController
public class PlaylistController {

    @Autowired
    private SpotifyService spotifySearchService;

    /**
     * Cerca le playlist e restituisce solo l'ID della prima trovata.
     *
     * @param query Il termine di ricerca (es. "Top Hits")
     * @return ResponseEntity contenente l'ID della playlist (String) o 404 Not Found.
     */
    @GetMapping("/search") // Abbiamo mantenuto lo stesso endpoint
    public ResponseEntity<String> search(@RequestParam String query) {

        // 1. Chiama il service che restituisce la lista
        List<PlaylistSimplified> playlists = spotifySearchService.searchPlaylists(query);

        // 2. Controlla se la lista non è vuota e ha almeno un elemento
        if (playlists != null && !playlists.isEmpty()) {

            // 3. Prendi l'ID del primo elemento (indice 0)
            String firstPlaylistId = playlists.get(0).getId();

            // 4. Restituisci l'ID con stato 200 OK
            return ResponseEntity.ok(firstPlaylistId);

        } else {

            // 5. Se la lista è vuota, restituisci 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}