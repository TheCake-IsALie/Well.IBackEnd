package org.generation.wellibackend.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.stereotype.Component;

// Import per il logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Component
@Getter
public class NewsConfigurator {

	// Aggiungi il logger
	private static final Logger logger = LoggerFactory.getLogger(NewsConfigurator.class);

	@NotNull
	private String apiKey, baseUrl;

	public NewsConfigurator() {
		File configFile = new File("NewsConfigurator.txt");

		try (Scanner input = new Scanner(configFile)) {

			if (input.hasNextLine()) {
				this.apiKey = input.nextLine().trim(); // Rimuove spazi
			} else {
				logger.error("ERRORE: NewsConfigurator.txt è incompleto (manca apiKey).");
			}

			if (input.hasNextLine()) {
				this.baseUrl = input.nextLine().trim(); // Rimuove spazi
			} else {
				logger.error("ERRORE: NewsConfigurator.txt è incompleto (manca baseUrl).");
			}

			// LOG DI CONFERMA
			logger.info("NewsConfigurator caricato. BaseURL: [{}], APIKey: [{}...]",
					baseUrl,
					apiKey != null ? apiKey.substring(0, 4) : "NULL");

		} catch (FileNotFoundException e) {
			logger.error("ERRORE: File di configurazione NewsConfigurator.txt non trovato!");
			logger.error("Percorso cercato: " + configFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
}