package org.generation.wellibackend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.generation.wellibackend.config.MeteoConfigurator;
import org.generation.wellibackend.model.dtos.CurrentWeatherDto;
import org.generation.wellibackend.model.dtos.MeteoResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class MeteoService {

	private static final Logger logger = LoggerFactory.getLogger(MeteoService.class);

	private final WebClient webClientMeteo;
	private final WebClient webClientGeocoding;

	@Autowired
	public MeteoService(MeteoConfigurator meteoConfig) {
		String baseUrl = Objects.requireNonNullElse(meteoConfig.getBaseUrl(), "https://ERRORE_CONFIGURAZIONE_METEO");
		String geocodingBaseUrl = Objects.requireNonNullElse(meteoConfig.getGeocodingBaseUrl(), "https://ERRORE_CONFIGURAZIONE_GEO");

		this.webClientMeteo = WebClient.builder().baseUrl(baseUrl).build();
		this.webClientGeocoding = WebClient.builder().baseUrl(geocodingBaseUrl).build();
	}


	public Mono<MeteoResponseDto> getWeatherByCityName(String cityName) {
		logger.info("Avvio ricerca meteo per la città: {}", cityName);

		return getCoordinates(cityName)
				.flatMap(coords -> {
					if (coords == null || coords.path("latitude").isMissingNode()) {
						logger.warn("Nessuna coordinata trovata per {}", cityName);
						return Mono.empty();
					}
					String lat = coords.path("latitude").asText();
					String lon = coords.path("longitude").asText();
					logger.info("Coordinate trovate per {}: Lat {}, Lon {}", cityName, lat, lon);

					return getWeatherByCoords(lat, lon);
				});
	}


	private Mono<JsonNode> getCoordinates(String cityName) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
				.path("/v1/search")
				.queryParam("name", cityName)
				.queryParam("count", 1);

		return this.webClientGeocoding.get()
				.uri(uriBuilder.build().toUriString())
				.retrieve()
				.bodyToMono(ObjectNode.class)
				.map(response -> response.path("results").get(0))
				.doOnError(e -> logger.error("Errore chiamata Geocoding API: {}", e.getMessage()));
	}


	private Mono<MeteoResponseDto> getWeatherByCoords(String latitude, String longitude) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
				.path("/v1/forecast")
				.queryParam("latitude", latitude)
				.queryParam("longitude", longitude)
				.queryParam("current_weather", "true")
				.queryParam("timezone", "auto");

		String finalUri = uriBuilder.build().toUriString();
		logger.info("Chiamata a Open-Meteo (WebClient): " + finalUri);

		return this.webClientMeteo.get()
				.uri(finalUri)
				.retrieve()
				.bodyToMono(ObjectNode.class)
				.map(this::mapToMeteoResponseDto)
				.doOnError(e -> logger.error("Errore chiamata MeteoAPI: {}", e.getMessage()));
	}


	private MeteoResponseDto mapToMeteoResponseDto(ObjectNode rootNode) {
		try {
			JsonNode dataCurrent = rootNode.path("current_weather");

			if (dataCurrent.isMissingNode()) {
				logger.error("La risposta JSON non contiene il blocco 'current_weather'");
				throw new RuntimeException("Dati meteo 'current_weather' mancanti");
			}

			double temp = dataCurrent.path("temperature").asDouble();
			int code = dataCurrent.path("weathercode").asInt();

			CurrentWeatherDto currentWeather = new CurrentWeatherDto();
			currentWeather.setTemperature(temp);
			currentWeather.setWeathercode(code);

			logger.info("Open-Meteo Dati ATTUALI estratti: Temp: {}, Code: {}", temp, code);
			return new MeteoResponseDto(currentWeather);

		} catch (Exception e) {
			logger.error("Errore durante il parsing della risposta JSON di Open-Meteo: {}", e.getMessage());
			throw new RuntimeException("Parsing JSON fallito", e);
		}
	}
}