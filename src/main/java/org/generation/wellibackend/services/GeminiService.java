package org.generation.wellibackend.services;

import com.google.genai.Client;
import com.google.genai.types.*;
import org.generation.wellibackend.model.entities.GeminiAI;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    private final GeminiAI geminiAI = new GeminiAI();

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
}