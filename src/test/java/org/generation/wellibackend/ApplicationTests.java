package org.generation.wellibackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.genai.Client;
import com.google.genai.types.*;
import org.generation.wellibackend.model.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
//        User user = new User();
//
//        user.getExtraData().put("firstName", "John");
//        data.remove("firstName");

        Client client = Client.builder().apiKey("AIzaSyBGULWUJ3wPi8N0p0XQJSUFkflNMUtwFEc").build();

        Content systemInstruction = Content.fromParts(Part.fromText("Sei un sito di news"));
        Tool googleSearchTool = Tool.builder().googleSearch(GoogleSearch.builder()).build();

        GenerateContentConfig config = GenerateContentConfig.builder()
                .systemInstruction(systemInstruction)
                .tools(googleSearchTool)
                .build();

        GenerateContentResponse response =
                client.models.generateContent("gemini-2.5-flash-lite", "Mi dai la notizia più importante di oggi?", config);

        // Gets the text string from the response by the quick accessor method `text()`.
        System.out.println("Unary response: " + response.text());



    }

}
