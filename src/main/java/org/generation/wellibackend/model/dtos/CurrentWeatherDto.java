package org.generation.wellibackend.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentWeatherDto
{
	@JsonProperty("temperature")
	private Double temperature;

	@JsonProperty("weathercode")
	private Integer weathercode;

	private String description;
}