// src/main/java/org/generation/wellibackend/services/NewsService.java
package org.generation.wellibackend.services;

import org.generation.wellibackend.model.dtos.NewsItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private final RestClient rest;
    private final String apiKey;

    public NewsService(
            RestClient.Builder builder,
            @Value("${newsapi.base-url}") String baseUrl,
            @Value("${newsapi.api-key}") String apiKey
    ) {
        this.rest = builder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    // 5 top nazionali (default), filtro per paese + lingua
    public List<NewsItemDto> topNational(String countryName, String lang, int limit) {
        String countryWikiUri = "http://en.wikipedia.org/wiki/" + countryName.replace(' ', '_');
        Map<String, Object> json = fetchArticles(Map.of(
                "resultType", "articles",
                "lang", lang,
                "sourceLocationUri", countryWikiUri,
                "articlesSortBy", "sourceAlexaGlobalRank",
                "articlesSortByAsc", "true",
                "articlesCount", Math.max(limit, 5),
                "apiKey", apiKey
        ));
        return pickTop(json, limit);
    }

    // 5 top globali per lingua (senza filtro paese)
    public List<NewsItemDto> topGlobal(String lang, int limit) {
        Map<String, Object> json = fetchArticles(Map.of(
                "resultType", "articles",
                "lang", lang,                              // es. "eng"
                "articlesSortBy", "sourceAlexaGlobalRank",
                "articlesSortByAsc", "true",
                "articlesCount", Math.max(limit, 5),
                "apiKey", apiKey
        ));
        return pickTop(json, limit);
    }

    private Map<String, Object> fetchArticles(Map<String, Object> params) {
        try {
            return rest.get()
                    .uri((UriBuilder ub) -> {
                        UriBuilder b = ub.path("/api/v1/article/getArticles");
                        params.forEach(b::queryParam);
                        return b.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new IllegalStateException("News provider unavailable", e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<NewsItemDto> pickTop(Map<String, Object> payload, int limit) {
        if (payload == null) return List.of();
        Object articlesObj = payload.get("articles");
        if (!(articlesObj instanceof Map)) return List.of();
        Map<String, Object> articles = (Map<String, Object>) articlesObj;
        Object resultsObj = articles.get("results");
        if (!(resultsObj instanceof List<?> list)) return List.of();

        return list.stream()
                .limit(limit)
                .map(it -> toDto((Map<String, Object>) it))
                .collect(Collectors.toList());
    }

    private NewsItemDto toDto(Map<String, Object> a) {
        NewsItemDto dto = new NewsItemDto();
        // Evita getOrDefault con wildcard: usa Objects.toString(get(...), default)
        dto.setTitle(Objects.toString(a.get("title"), ""));
        dto.setUrl(Objects.toString(a.get("url"), ""));
        dto.setDate(Objects.toString(
                Optional.ofNullable(a.get("dateTime")).orElse(a.get("date")), "")
        );
        dto.setLang(Objects.toString(a.get("lang"), ""));

        String sourceName = "";
        Object src = a.get("source");
        if (src instanceof Map<?, ?> sm) {
            Object title = sm.get("title");
            Object uri = sm.get("uri");
            sourceName = Objects.toString(
                    title != null ? title : uri, ""
            );
        }
        dto.setSource(sourceName);
        return dto;
    }
}
