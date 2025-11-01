package org.generation.wellibackend.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Component
@Getter
public class MeteoConfigurator {

	private static final Logger logger = LoggerFactory.getLogger(MeteoConfigurator.class);

	@NotNull
	private String baseUrl;

	@NotNull
	private String geocodingBaseUrl;

	public MeteoConfigurator() {
		File configFile = new File("MeteoConfigurator.txt");

		try (Scanner input = new Scanner(configFile)) {

			if (input.hasNextLine()) {
				this.baseUrl = input.nextLine().trim();
			} else {
				logger.error("ERRORE: MeteoConfigurator.txt è incompleto (manca baseUrl).");
			}

			if (input.hasNextLine()) {
				this.geocodingBaseUrl = input.nextLine().trim(); 
			} else {
				logger.error("ERRORE: MeteoConfigurator.txt è incompleto (manca geocodingBaseUrl).");
			}

			logger.info("MeteoConfigurator caricato. BaseURL: [{}], GeocodingURL: [{}]", baseUrl, geocodingBaseUrl);

		} catch (FileNotFoundException e) {
			logger.error("ERRORE: File di configurazione MeteoConfigurator.txt non trovato!");
			logger.error("Percorso cercato: " + configFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
}