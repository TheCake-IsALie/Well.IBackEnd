package org.generation.wellibackend.services;

import org.generation.wellibackend.config.NewsConfigurator;
import org.generation.wellibackend.model.dtos.NewsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

// Import per il logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NewsService {

	private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

	private final WebClient webClient;
	private final NewsConfigurator newsConfig;

	@Autowired
	public NewsService(NewsConfigurator newsConfig) {
		this.newsConfig = newsConfig;

		String baseUrl = newsConfig.getBaseUrl();
		if (baseUrl == null) {
			logger.error("BaseUrl di NewsAPI è NULL. Impossibile inizializzare WebClient.");
			baseUrl = "https://ERRORE_CONFIGURAZIONE";
		} else {
			// Logica difensiva per pulire l'URL
			while (baseUrl.endsWith("/")) {
				baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
			}
		}

		this.webClient = WebClient.builder()
				.baseUrl(baseUrl)
				.build();
	}

	/**
	 * Logica aggiornata:
	 * 1. Se 'q' (query) è presente, ha la priorità e usa /everything.
	 * 2. Se 'q' è assente e 'category' è presente, usa /top-headlines con la categoria.
	 * 3. Se entrambi sono assenti, usa /top-headlines con un default (country=us).
	 */
	public Mono<NewsResponseDto> getNews(String query, String category) {

		String defaultLanguage = "it";
		// NOTA: 'us' è l'unico paese affidabile sul piano gratuito
		String defaultCountry = "us";

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		uriBuilder.queryParam("apiKey", newsConfig.getApiKey());

		if (query != null && !query.isEmpty()) {
			// --- CASO 1: Ricerca per CONTENUTO (q) ---
			logger.info("Filtro per Contenuto (q): {}", query);
			uriBuilder.path("/everything");
			uriBuilder.queryParam("q", query);
			uriBuilder.queryParam("language", defaultLanguage);
			uriBuilder.queryParam("sortBy", "popularity");

		} else if (category != null && !category.isEmpty()) {
			// --- CASO 2: Ricerca per CATEGORIA (category) ---
			logger.info("Filtro per Categoria: {}", category);
			uriBuilder.path("/top-headlines");
			uriBuilder.queryParam("category", category);
			// NOTA: Dobbiamo comunque specificare un paese per /top-headlines
			// Usiamo 'us' perché 'it' non è supportato
			uriBuilder.queryParam("country", defaultCountry);

		} else {
			// --- CASO 3: Default (nessun filtro) ---
			logger.info("Nessun filtro, uso default (country=us, category=general)");
			uriBuilder.path("/top-headlines");
			uriBuilder.queryParam("country", defaultCountry);
			uriBuilder.queryParam("category", "general"); // Categoria generica
		}

		String finalUri = uriBuilder.build().toUriString();
		logger.info("Chiamata a NewsAPI (WebClient): " + finalUri);

		return this.webClient.get()
				.uri(finalUri)
				.retrieve()
				.bodyToMono(NewsResponseDto.class);
	}
}