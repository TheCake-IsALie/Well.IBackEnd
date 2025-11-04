package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.entities.Mood;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.services.SpotifyService;
import org.generation.wellibackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importato
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.List;

@RestController
@RequestMapping("/api/spotify")
public class PlaylistController {

    @Autowired
    private SpotifyService spotifySearchService;
    @Autowired
    private UserService userService;

    /**
     * Cerca le playlist e restituisce solo l'ID della prima trovata.
     *
     * @return ResponseEntity contenente l'ID della playlist (String) o 404 Not Found.
     */
    @GetMapping("/playlist")
    public ResponseEntity<String> search(@AuthenticationPrincipal User user) {
        Mood mood = userService.getMood(user);
        switch (mood.getMood().toLowerCase()) {
            case "happy":
                return ResponseEntity.ok("37i9dQZF1EIgG2NEOhqsD7");
            case "peaceful":
                return ResponseEntity.ok("37i9dQZF1EIhnGUyOEDCHI");
            case "sad":
                return ResponseEntity.ok("37i9dQZF1EIg85EO6f7KwU");
            case "neutral":
                return ResponseEntity.ok("37i9dQZF1EIcJuX6lvhrpW");
            case "angry":
                return ResponseEntity.ok("37i9dQZF1EIgNZCaOGb0Mi");
            case "anxious":
                return ResponseEntity.ok("37i9dQZF1EIeNoBMmwUjGA");
            default:
                return ResponseEntity.ok("37i9dQZF1EIdDn5P759aRj");
        }
    }
}