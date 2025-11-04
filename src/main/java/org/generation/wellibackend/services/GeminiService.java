package org.generation.wellibackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.*;
import org.generation.wellibackend.model.dtos.PhraseDto;
import org.generation.wellibackend.model.entities.GeminiAI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    private final GeminiAI geminiAI = new GeminiAI();
    private final ObjectMapper objectMapper = new ObjectMapper();



    //Configurazione di Gemini AI
    Client client = Client.builder().apiKey(geminiAI.getApiKey()).build();

    Content systemInstruction = Content.fromParts(Part.fromText(geminiAI.getConfig()));
    Tool googleSearchTool = Tool.builder().googleSearch(GoogleSearch.builder()).build();

    GenerateContentConfig config = GenerateContentConfig.builder()
            .systemInstruction(systemInstruction)
            .tools(googleSearchTool)
            .build();

    /***
     * @param prompt Stringa di input per Gemini AI; Per una risposta breve usare #!!# all'inizio e alla fine della richiesta
     * @Example: "#!!# Dammi una frase motivazionale famosa #!!#"
     * @return Stringa di risposta generata da Gemini AI
     ***/
    public String getResponseGemini(String prompt) {
        GenerateContentResponse response = client.models.generateContent(geminiAI.getModel(), prompt, config);
        return response.text();
    }

    public PhraseDto getMotivationalQuoteByMood(String mood) {
        String prompt = "#!!# Genera una famosa frase motivazionale adatta a una persona che si sente '" + mood + "'. " +
                "La tua risposta DEVE essere solo ed esclusivamente un oggetto JSON valido con due chiavi: " +
                "'phrase' (la frase) e 'author' (l'autore). " +
                "Ad esempio: {\"phrase\": \"La vita è...\", \"author\": \"Nome Autore\"} #!!#";

        try {
            String jsonResponse = getResponseGemini(prompt);

            // Pulisci la risposta di Gemini se include markdown (```json ... ```)
            jsonResponse = jsonResponse.replace("```json", "").replace("```", "").trim();

            // Parsa la stringa JSON nel nostro DTO
            return objectMapper.readValue(jsonResponse, PhraseDto.class);

        } catch (Exception e) {
            // Fallback in caso di errore di Gemini o parsing
            e.printStackTrace(); // Logga l'errore
            return new PhraseDto("Il sorriso è il sole che scaccia l'inverno dal viso umano.", "Victor Hugo");
        }
    }
}