package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.NewsResponseDto;
import org.generation.wellibackend.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/news")
public class NewsController {

	@Autowired
	private NewsService newsService;

	@GetMapping("/headlines")
	public Mono<ResponseEntity<NewsResponseDto>> getNews(
			// Filtro per contenuto (es. "tecnologia")
			@RequestParam(required = false) String q,

			// NUOVO: Filtro per categoria (es. "sports", "technology")
			@RequestParam(required = false) String category
	) {
		return newsService.getNews(q, category)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}