package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.RecipeSuggestionDto;
import org.generation.wellibackend.model.dtos.RecipeSuggestionsResponse;
import org.generation.wellibackend.services.RecipeSuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
public class RecipeSuggestionController {

    private final RecipeSuggestionService service;

    public RecipeSuggestionController(RecipeSuggestionService service) {
        this.service = service;
    }

    @PostMapping("/suggest")
    public ResponseEntity<RecipeSuggestionsResponse> suggest(@RequestBody RecipeSuggestionDto body) {
        if (body.getIngredients() == null || body.getIngredients().isEmpty()) {
            return ResponseEntity.badRequest().body(new RecipeSuggestionsResponse());
        }
        return ResponseEntity.ok(service.suggest(body));
    }
}
