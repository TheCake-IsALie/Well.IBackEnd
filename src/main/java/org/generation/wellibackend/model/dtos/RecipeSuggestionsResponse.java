package org.generation.wellibackend.model.dtos;

import java.util.List;

public class RecipeSuggestionsResponse {
    private List<RecipeSuggestionItemDto> recipes;
    private String aiTextFallback;

    public List<RecipeSuggestionItemDto> getRecipes() { return recipes; }
    public void setRecipes(List<RecipeSuggestionItemDto> recipes) { this.recipes = recipes; }
    public String getAiTextFallback() { return aiTextFallback; }
    public void setAiTextFallback(String aiTextFallback) { this.aiTextFallback = aiTextFallback; }
}
