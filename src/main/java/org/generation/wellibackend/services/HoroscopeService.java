package org.generation.wellibackend.services;

import org.generation.wellibackend.model.dtos.HoroscopeResponseDto;
import org.generation.wellibackend.model.entities.HoroscopeEntry;
import org.generation.wellibackend.model.enums.HoroscopeScope;
import org.generation.wellibackend.model.repositories.HoroscopeEntryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Service
public class HoroscopeService {

    private final GeminiService gemini;
    private final HoroscopeEntryRepository repo;

    public HoroscopeService(GeminiService gemini, HoroscopeEntryRepository repo) {
        this.gemini = gemini; this.repo = repo;
    }

    public HoroscopeResponseDto getOnDemand(String rawSign, String rawScope, String rawTz) {
        String sign = normalizeSign(rawSign);
        HoroscopeScope scope = parseScope(rawScope);
        ZoneId zone = parseZoneOrEuropeRome(rawTz);
        String periodKey = computePeriodKey(scope, zone);

        return repo.findBySignAndScopeAndPeriodKey(sign, scope, periodKey)
                .map(this::toDto)
                .orElseGet(() -> generateSaveAndReturn(sign, scope, periodKey, zone));
    }

    private String computePeriodKey(HoroscopeScope scope, ZoneId zone) {
        LocalDate today = LocalDate.now(zone);
        switch (scope) {
            case DAILY -> { return today.format(DateTimeFormatter.ISO_DATE); }
            case WEEKLY -> {
                WeekFields wf = WeekFields.ISO;
                int wby = today.get(wf.weekBasedYear());
                int wow = today.get(wf.weekOfWeekBasedYear());
                return "%d-W%02d".formatted(wby, wow);
            }
            case MONTHLY -> { return YearMonth.from(today).toString(); }
            case YEARLY -> { return String.valueOf(Year.from(today).getValue()); }
            default -> throw new IllegalArgumentException("Scope non valido");
        }
    }

    private HoroscopeResponseDto generateSaveAndReturn(String sign, HoroscopeScope scope, String periodKey, ZoneId zone) {
        String prompt =
                """
                Scrivi un oroscopo in italiano per il segno: %s e per il periodo: %s.
                Requisiti:
                - Massimo 200 parole totali, tono pratico e sintetico, senza emoji.
                - Tre sezioni in questo esatto formato (una riga di intestazione seguita dal testo):
                  Lavoro:
                  Salute:
                  Amore:
                - Evita predizioni assolute o mediche; fornisci spunti concreti e realistici.
                """.formatted(sign, scope.name().toLowerCase());

        String text = safeGenerate(prompt);

        String work = extractSection(text, "Lavoro:");
        String health = extractSection(text, "Salute:");
        String love = extractSection(text, "Amore:");

        for (int i = 0; i < 2; i++) {
            try {
                HoroscopeEntry e = new HoroscopeEntry();
                e.setSign(sign);
                e.setScope(scope);
                e.setPeriodKey(periodKey);
                e.setTextWork(work);
                e.setTextHealth(health);
                e.setTextLove(love);
                e.setCreatedAt(OffsetDateTime.now(zone));
                e.setUpdatedAt(e.getCreatedAt());
                e = repo.saveAndFlush(e);
                return toDto(e);
            } catch (DataIntegrityViolationException dup) {
                return repo.findBySignAndScopeAndPeriodKey(sign, scope, periodKey).map(this::toDto).orElse(null);
            }
        }
        return repo.findBySignAndScopeAndPeriodKey(sign, scope, periodKey).map(this::toDto).orElse(null);
    }

    private String safeGenerate(String prompt) {
        try {
            return gemini.getResponseGemini(prompt);
        } catch (Exception ex) {
            return """
      Lavoro: Concentrati su una priorità alla volta e comunica in modo chiaro: piccoli avanzamenti contano.
      Salute: Rispetta i ritmi: idratazione, pause brevi e sonno regolare ti sostengono.
      Amore: Ascolto e gentilezza: costruisci armonia con gesti semplici e presenza autentica.
      """;
        }
    }

    // Estrazione robuste delle sezioni tra le intestazioni richieste
    private String extractSection(String text, String header) {
        String[] lines = text.split("\\r?\\n");
        StringBuilder buf = new StringBuilder();
        boolean on = false;
        for (String line : lines) {
            String s = line.strip();
            if (s.equalsIgnoreCase(header)) { on = true; continue; }
            if (on && s.matches("(?i)^(Lavoro|Salute|Amore):$")) break;
            if (on) buf.append(line).append(' ');
        }
        String out = buf.toString().trim();
        if (out.isEmpty()) out = "Sintesi breve non disponibile.";
        return out;
    }

    private HoroscopeResponseDto toDto(HoroscopeEntry e) {
        HoroscopeResponseDto d = new HoroscopeResponseDto();
        d.setSign(e.getSign());
        d.setScope(e.getScope().name());
        d.setPeriodKey(e.getPeriodKey());
        d.setWork(e.getTextWork());
        d.setHealth(e.getTextHealth());
        d.setLove(e.getTextLove());
        return d;
    }

    private String normalizeSign(String s) {
        if (s == null || s.isBlank()) return "Generico";
        String t = s.trim();
        return Character.toUpperCase(t.charAt(0)) + t.substring(1).toLowerCase();
    }

    private HoroscopeScope parseScope(String s) {
        if (s == null) return HoroscopeScope.DAILY;
        return switch (s.trim().toUpperCase(Locale.ITALY)) {
            case "DAILY", "GIORNALIERO" -> HoroscopeScope.DAILY;
            case "WEEKLY", "SETTIMANALE" -> HoroscopeScope.WEEKLY;
            case "MONTHLY", "MENSILE" -> HoroscopeScope.MONTHLY;
            case "YEARLY", "ANNUALE" -> HoroscopeScope.YEARLY;
            default -> HoroscopeScope.DAILY;
        };
    }

    private ZoneId parseZoneOrEuropeRome(String tz) {
        try {
            return (tz == null || tz.isBlank()) ? ZoneId.of("Europe/Rome") : ZoneId.of(tz);
        } catch (Exception e) {
            return ZoneId.of("Europe/Rome");
        }
    }
}
