package org.generation.wellibackend.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.generation.wellibackend.model.dtos.SourceDto;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDto {
	private SourceDto source;
	private String author;
	private String title;
	private String description;
	private String url;
	private String urlToImage;
	private String publishedAt;
	private String content;
}