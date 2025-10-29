package org.generation.wellibackend.model.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
	private String description;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime start;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime end;

	private String type;
}
