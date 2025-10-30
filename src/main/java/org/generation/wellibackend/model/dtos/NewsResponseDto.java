package org.generation.wellibackend.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsResponseDto {
	private String status;
	private int totalResults;
	private List<ArticleDto> articles;
}