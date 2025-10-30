package org.generation.wellibackend.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.generation.wellibackend.model.dtos.RecipeSuggestionDto;
import org.generation.wellibackend.model.dtos.RecipeSuggestionItemDto;
import org.generation.wellibackend.model.dtos.RecipeSuggestionsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeSuggestionService {

    private final GeminiService geminiService; // esistente nel progetto
    private final ObjectMapper mapper;

    public RecipeSuggestionService(GeminiService geminiService) {
        this.geminiService = geminiService;
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public RecipeSuggestionsResponse suggest(RecipeSuggestionDto req) {
        RecipeSuggestionsResponse out = new RecipeSuggestionsResponse();
        String prompt = buildPrompt(req);
        String ai = safeGenerate(prompt);

        // 1) Prova parse diretto in wrapper
        try {
            RecipeSuggestionsResponse parsed = mapper.readValue(ai, RecipeSuggestionsResponse.class);
            if (parsed.getRecipes() != null && !parsed.getRecipes().isEmpty()) return parsed;
        } catch (Exception ignore) { }

        // 2) Prova a leggere solo "recipes"
        try {
            JsonNode node = mapper.readTree(ai);
            if (node.has("recipes")) {
                List<RecipeSuggestionItemDto> list = mapper.readerForListOf(RecipeSuggestionItemDto.class)
                        .readValue(node.get("recipes"));
                if (list != null && !list.isEmpty()) { out.setRecipes(list); return out; }
            }
        } catch (Exception ignore2) { }

        // 3) Fallback testuale
        out.setAiTextFallback(ai);
        return out;
    }

    private String buildPrompt(RecipeSuggestionDto req) {
        String ingredients = String.join(", ", req.getIngredients() == null ? List.of() : req.getIngredients());
        String exclude = String.join(", ", req.getExclude() == null ? List.of() : req.getExclude());
        String allergens = String.join(", ", req.getAllergens() == null ? List.of() : req.getAllergens());
        String cuisine = req.getCuisine() != null && !req.getCuisine().isBlank() ? req.getCuisine() : "nessuna preferenza";
        int maxTime = req.getMaxTimeMinutes() != null ? req.getMaxTimeMinutes() : 30;
        int servings = req.getServings() != null ? req.getServings() : 2;

        return """
      Sei uno chef assistente: proponi 2-3 ricette che usino PRINCIPALMENTE questi ingredienti disponibili:
      - ingredients: [%s]
      Vincoli:
      - Evita questi ingredienti: [%s]
      - Evita allergeni: [%s]
      - Cucina preferita: %s
      - Tempo massimo: %d minuti
      - Porzioni: %d
      - Consentiti come staples: acqua, olio d'oliva, sale, pepe, spezie/erbe comuni.
      - Se mancano elementi, suggerisci extraSuggested come facoltativi o alternative.
      - Niente alcolici o tecniche complesse; passi chiari e brevi.
      - LINGUA: rispondi in italiano.

      OUTPUT: restituisci SOLO JSON valido UTF-8 con questo schema (nessun testo fuori dal JSON):
      {
        "recipes": [
          {
            "title": "string",
            "timeMinutes": number,
            "servings": number,
            "ingredientsUsed": ["string"],
            "extraSuggested": ["string"],
            "steps": ["string"]
          }
        ]
      }
      """.formatted(ingredients, exclude, allergens, cuisine, maxTime, servings);
    }

    private String safeGenerate(String prompt) {
        try {
            return geminiService.getResponseGemini(prompt);
        } catch (Exception ex) {
            return """
      {
        "recipes": [
          {
            "title": "Frittata veloce di verdure",
            "timeMinutes": 15,
            "servings": 2,
            "ingredientsUsed": ["uova", "zucchine", "cipolla"],
            "extraSuggested": ["formaggio grattugiato"],
            "steps": ["Sbatti le uova con sale e pepe", "Soffriggi cipolla e zucchine", "Unisci le uova e cuoci fino a doratura"]
          }
        ]
      }
      """;
        }
    }
}
