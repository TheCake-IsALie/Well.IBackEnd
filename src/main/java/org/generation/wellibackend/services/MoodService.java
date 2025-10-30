// src/main/java/org/generation/wellibackend/services/MoodService.java
package org.generation.wellibackend.services;

import org.generation.wellibackend.model.dtos.MoodResponseDto;
import org.generation.wellibackend.model.entities.MoodEntry;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.model.repositories.MoodEntryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.UUID;

@Service
public class MoodService {

    private final UserService userService;
    private final GeminiService geminiService;
    private final MoodEntryRepository repo;

    public MoodService(UserService userService, GeminiService geminiService, MoodEntryRepository repo) {
        this.userService = userService;
        this.geminiService = geminiService;
        this.repo = repo;
    }

    public MoodResponseDto saveTodayMood(User u, String moodText) {
        ZoneId zone = ZoneId.of("Europe/Rome");
        LocalDate today = LocalDate.now(zone);

        String aiSummary = geminiService.getResponseGemini("Riassumi brevemente il seguente mood: " + moodText);

        for (int i = 0; i < 2; i++) {
            try {
                MoodEntry e = repo.findByUserIdAndDay(u.getId(), today).orElseGet(MoodEntry::new);
                if (e.getId() == null) {
                    e.setUserId(u.getId());
                    e.setDay(today);
                    e.setCreatedAt(OffsetDateTime.now(zone));
                }
                e.setMoodText(moodText);
                e.setAiSummary(aiSummary);
                e.setUpdatedAt(OffsetDateTime.now(zone));
                e = repo.saveAndFlush(e);
                return toDto(e);
            } catch (DataIntegrityViolationException ex) {
                // retry su collisione (vincolo unico user_id+day)
            }
        }
        return repo.findByUserIdAndDay(u.getId(), today).map(this::toDto).orElse(null);
    }

    public MoodResponseDto getTodayMood(User u) {
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Rome"));
        return repo.findByUserIdAndDay(u.getId(), today).map(this::toDto).orElse(null);
    }

    private MoodResponseDto toDto(MoodEntry e) {
        MoodResponseDto d = new MoodResponseDto();
        d.setUserId(e.getUserId());
        d.setMoodText(e.getMoodText());
        d.setAiSummary(e.getAiSummary());
        d.setDay(e.getDay());
        return d;
    }
}
