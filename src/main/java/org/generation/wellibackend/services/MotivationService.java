// src/main/java/org/generation/wellibackend/services/MotivationService.java
package org.generation.wellibackend.services;

import org.generation.wellibackend.model.dtos.MotivationResponseDto;
import org.generation.wellibackend.model.entities.MotivationEntry;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.model.repositories.MotivationEntryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class MotivationService {

    private final UserService userService;
    private final GeminiService geminiService;
    private final MotivationEntryRepository repo;

    public MotivationService(UserService userService, GeminiService geminiService, MotivationEntryRepository repo) {
        this.userService = userService; this.geminiService = geminiService; this.repo = repo;
    }

    public MotivationResponseDto getTodayOnDemand(String token) {
        User u = userService.findUserByToken(token);
        ZoneId zone = ZoneId.of("Europe/Rome");
        LocalDate today = LocalDate.now(zone);

        return repo.findByUserIdAndDay(u.getId(), today)
                .map(this::toDto)
                .orElseGet(() -> generateSaveReturn(u, today, zone));
    }

    private MotivationResponseDto generateSaveReturn(User u, LocalDate today, ZoneId zone) {
        String prompt = "Scrivi una frase motivazionale breve (<=160 caratteri), positiva, in italiano, senza emoji.";
        String text = geminiService.getResponseGemini(prompt);

        for (int i = 0; i < 2; i++) {
            try {
                MotivationEntry e = new MotivationEntry();
                e.setUserId(u.getId());
                e.setDay(today);
                e.setText(text);
                e.setCreatedAt(OffsetDateTime.now(zone));
                e.setUpdatedAt(e.getCreatedAt());
                e = repo.saveAndFlush(e);
                return toDto(e);
            } catch (DataIntegrityViolationException ex) {
                // Race: qualcun altro l'ha creata, rilegge
                return repo.findByUserIdAndDay(u.getId(), today).map(this::toDto).orElse(null);
            }
        }
        return repo.findByUserIdAndDay(u.getId(), today).map(this::toDto).orElse(null);
    }

    private MotivationResponseDto toDto(MotivationEntry e) {
        MotivationResponseDto d = new MotivationResponseDto();
        d.setUserId(e.getUserId());
        d.setText(e.getText());
        d.setDay(e.getDay());
        return d;
    }
}
