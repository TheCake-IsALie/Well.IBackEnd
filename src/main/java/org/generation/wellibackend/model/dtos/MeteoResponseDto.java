package org.generation.wellibackend.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeteoResponseDto {

	@JsonProperty("current")
	private CurrentWeatherDto currentWeather;

	public MeteoResponseDto() {}

	public MeteoResponseDto(CurrentWeatherDto currentWeather) {
		this.currentWeather = currentWeather;
	}
}