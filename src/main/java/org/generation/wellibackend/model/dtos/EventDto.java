package org.generation.wellibackend.model.dtos;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
public class EventDto
{
	private String title;
	private String description;
	private String type;

	private LocalDateTime start;

	private LocalDateTime end;
}
