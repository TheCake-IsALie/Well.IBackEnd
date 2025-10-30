package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.MoodRequestDto;
import org.generation.wellibackend.model.dtos.MoodResponseDto;
import org.generation.wellibackend.services.MoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mood")
public class MoodController {

    private final MoodService moodService;

    public MoodController(MoodService moodService) {
        this.moodService = moodService;
    }

    private String extractToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) return null;
        return authorization.substring(7);
    }

    @PostMapping
    public ResponseEntity<MoodResponseDto> setMood(
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @RequestBody MoodRequestDto req) {

        String token = extractToken(authorization);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MoodResponseDto body = moodService.saveTodayMood(token, req.getMoodText());
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<MoodResponseDto> getMood(
            @RequestHeader(name = "Authorization", required = true) String authorization) {

        String token = extractToken(authorization);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MoodResponseDto body = moodService.getTodayMood(token);
        return (body == null) ? ResponseEntity.noContent().build() : ResponseEntity.ok(body);
    }
}
