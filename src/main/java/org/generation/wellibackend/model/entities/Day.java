package org.generation.wellibackend.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Day
{
	@Id
	private LocalDate date;

	@OneToMany(mappedBy = "date", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Event> events = new ArrayList<>();
}
