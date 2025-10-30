package org.generation.wellibackend.model.dtos;

import java.util.List;

public class RecipeSuggestionItemDto {
    private String title;
    private Integer timeMinutes;
    private Integer servings;
    private List<String> ingredientsUsed;
    private List<String> extraSuggested;
    private List<String> steps;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getTimeMinutes() { return timeMinutes; }
    public void setTimeMinutes(Integer timeMinutes) { this.timeMinutes = timeMinutes; }
    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }
    public List<String> getIngredientsUsed() { return ingredientsUsed; }
    public void setIngredientsUsed(List<String> ingredientsUsed) { this.ingredientsUsed = ingredientsUsed; }
    public List<String> getExtraSuggested() { return extraSuggested; }
    public void setExtraSuggested(List<String> extraSuggested) { this.extraSuggested = extraSuggested; }
    public List<String> getSteps() { return steps; }
    public void setSteps(List<String> steps) { this.steps = steps; }
}
