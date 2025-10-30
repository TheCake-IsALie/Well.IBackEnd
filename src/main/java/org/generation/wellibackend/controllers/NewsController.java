package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.NewsItemDto;
import org.generation.wellibackend.services.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService news;

    public NewsController(NewsService news) { this.news = news; }

    @GetMapping
    public ResponseEntity<Map<String, Object>> get(
            @RequestParam(value = "country", defaultValue = "Italy") String country,
            @RequestParam(value = "langNational", defaultValue = "ita") String langNational,
            @RequestParam(value = "langGlobal", defaultValue = "eng") String langGlobal,
            @RequestParam(value = "limit", defaultValue = "5") int limit
    ) {
        if (limit < 1 || limit > 20) limit = 5;
        List<NewsItemDto> national = news.topNational(country, langNational, limit);
        List<NewsItemDto> global = news.topGlobal(langGlobal, limit);
        return ResponseEntity.ok(Map.of("national", national, "global", global));
    }
}
