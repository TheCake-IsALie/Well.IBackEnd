package org.generation.wellibackend.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campi JSON che non mappiamo
public class SourceDto {
	private String id;
	private String name;
}