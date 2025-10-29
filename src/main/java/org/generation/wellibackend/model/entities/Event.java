package org.generation.wellibackend.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.generation.wellibackend.model.enums.EventStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity
{
	@NotNull @NotBlank
	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String description = "Nessuna descrizione";

	@NotNull
	@Column(nullable = false)
	private LocalDateTime start;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime end;

	private String type = "Nessuno";

	@NotNull
	private boolean isAllDay = false;

	private String color = "#89b2f3";

	@Enumerated(EnumType.STRING)
	private EventStatus showAs = EventStatus.BUSY;

	private Integer reminderMinutes; // Nullable

	private String recurrenceRule; // Per FullCalendar (formato RRule iCal)

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore // Evita loop infiniti nella serializzazione
	private User user;
}
