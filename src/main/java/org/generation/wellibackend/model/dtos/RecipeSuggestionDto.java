package org.generation.wellibackend.model.dtos;

import java.util.List;

public class RecipeSuggestionDto {
    private List<String> ingredients;
    private List<String> exclude;
    private List<String> allergens;
    private String cuisine;
    private Integer maxTimeMinutes;
    private Integer servings;

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    public List<String> getExclude() { return exclude; }
    public void setExclude(List<String> exclude) { this.exclude = exclude; }
    public List<String> getAllergens() { return allergens; }
    public void setAllergens(List<String> allergens) { this.allergens = allergens; }
    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public Integer getMaxTimeMinutes() { return maxTimeMinutes; }
    public void setMaxTimeMinutes(Integer maxTimeMinutes) { this.maxTimeMinutes = maxTimeMinutes; }
    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }
}
