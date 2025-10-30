package org.generation.wellibackend.services;

import org.generation.wellibackend.model.dtos.LifestyleAdviceDto;
import org.generation.wellibackend.model.dtos.LifestyleInputDto;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LifestyleAdviceService {

    private final GeminiService geminiService;

    public LifestyleAdviceService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public LifestyleAdviceDto evaluate(LifestyleInputDto in) {
        // Normalizza input
        int kcal = nullSafeInt(in.getKcalToday(), 0);
        double standH = nullSafeDouble(in.getHoursStanding(), 0.0);
        double outH = nullSafeDouble(in.getHoursOutdoors(), 0.0);
        int mindMin = nullSafeInt(in.getMindfulnessMinutes(), 0);
        String sex = (in.getSex() == null ? "" : in.getSex().trim().toUpperCase(Locale.ITALY));

        // Baseline
        Integer kcalRef = switch (sex) {
            case "MASCHIO", "MALE", "M" -> 2500;
            case "FEMMINA", "FEMALE", "F" -> 2000;
            default -> null;
        };
        String standingRef = "~5h10m/giorno";
        String outdoorRef = ">=120 min/settimana";
        String mindfulnessRef = ">=10 min/giorno";

        // Prompt per Gemini
        String prompt = """
      Sei un assistente benessere: confronta i dati con medie/obiettivi umani e fornisci un feedback conciso, pratico e NON medico, in italiano.
      Dati utente (oggi):
      - kcal: %d
      - ore in piedi: %.2f
      - ore all'aperto: %.2f
      - mindfulness (min): %d
      - sesso: %s

      Riferimenti da usare:
      - Calorie: %s
      - Standing: %s
      - Outdoor: %s
      - Mindfulness: %s

      Istruzioni di output:
      - 3-4 frasi totali, tono incoraggiante e concreto.
      - Indica dove è sopra/sotto rispetto al riferimento senza allarmismi.
      - Suggerisci 1 micro-azione per area scostata (max 1 per area).
      - Non includere consigli clinici o nutrizionali specifici.
      """.formatted(
                kcal, standH, outH, mindMin, (sex.isBlank() ? "N/D" : sex),
                (kcalRef == null ? "n.d. per questa categoria" : (kcalRef + " kcal/giorno")),
                standingRef, outdoorRef, mindfulnessRef
        );

        String advice;
        try {
            advice = geminiService.getResponseGemini(prompt);
        } catch (Exception ex) {
            advice = "Oggi punta a scelte semplici: alzati più spesso, esci per una breve passeggiata e dedica qualche minuto alla respirazione consapevole.";
        }

        LifestyleAdviceDto out = new LifestyleAdviceDto();
        out.setAdvice(advice);
        out.setKcalToday(kcal);
        out.setHoursStanding(standH);
        out.setHoursOutdoors(outH);
        out.setMindfulnessMinutes(mindMin);
        out.setSex(sex);
        out.setKcalRef(kcalRef);
        out.setStandingRef(standingRef);
        out.setOutdoorRef(outdoorRef);
        out.setMindfulnessRef(mindfulnessRef);
        return out;
    }

    private int nullSafeInt(Integer v, int def) { return v == null ? def : Math.max(v, 0); }
    private double nullSafeDouble(Double v, double def) { return v == null ? def : Math.max(v, 0.0); }
}
