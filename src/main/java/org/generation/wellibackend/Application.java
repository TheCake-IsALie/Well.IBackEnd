package org.generation.wellibackend;

import org.generation.wellibackend.services.GeminiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        //GeminiService gemini = new GeminiService();
        //System.out.println(gemini.getResponseGemini("#!!# DAMMI L'ID DELLA MIA PLAYLIST SPOTIFY PREFERITA #!!#"));

    }

}
