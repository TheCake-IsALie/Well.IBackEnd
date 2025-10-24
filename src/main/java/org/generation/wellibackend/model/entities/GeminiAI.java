package org.generation.wellibackend.model.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Getter
@Setter
public class GeminiAI {
    @NotNull
    private String apiKey, model, config;

//    API-KEY Personale
//    gemini-2.5-flash-lite
//    Se trovi i simboli #!!# come inizio e fine del testo, mi devi restituire in modo esplicito SOLO quello che ti chiedo, senza intercalari o altro. Se non trovi i simboli, rispondi normalmente senza dilungarti troppo, ma la informazione che dai deve essere completa e soprattutto CORRETTA."
    public GeminiAI() {
        try (Scanner input = new Scanner(new File("Gemini-Configurator.txt"))) {
            this.apiKey = input.nextLine();
            this.model = input.nextLine();
            this.config = input.nextLine();
        } catch (FileNotFoundException e) {
            System.err.println("ERRORE: File di configurazione non trovato!");
            e.printStackTrace();
        }
    }
}
