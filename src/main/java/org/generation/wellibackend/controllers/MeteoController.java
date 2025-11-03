package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.MeteoResponseDto;
import org.generation.wellibackend.services.MeteoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/meteo")
public class MeteoController {

	@Autowired
	private MeteoService meteoService;

	@GetMapping("/city")
	public Mono<ResponseEntity<MeteoResponseDto>> getWeatherByCity(
			@RequestParam String name
	) {
		return meteoService.getWeatherByCityName(name)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}