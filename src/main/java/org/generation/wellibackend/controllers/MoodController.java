package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.MoodRequestDto;
import org.generation.wellibackend.model.dtos.MoodResponseDto;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.services.MoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/mood")
public class MoodController {

    private final MoodService moodService;

    public MoodController(MoodService moodService) {
        this.moodService = moodService;
    }


    @PostMapping
    public ResponseEntity<MoodResponseDto> setMood(
            @AuthenticationPrincipal User u,
            @RequestBody MoodRequestDto req) {


        MoodResponseDto body = moodService.saveTodayMood(u, req.getMoodText());
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<MoodResponseDto> getMood(@AuthenticationPrincipal User u)
    {
        MoodResponseDto body = moodService.getTodayMood(u);
        return (body == null) ? ResponseEntity.noContent().build() : ResponseEntity.ok(body);
    }
}
