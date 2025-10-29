package org.generation.wellibackend.services;

import org.generation.wellibackend.exceptions.InvalidCredentials;
import org.generation.wellibackend.model.dtos.EventDto;
import org.generation.wellibackend.model.entities.Event;
import org.generation.wellibackend.model.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService
{
	@Autowired
	private EventRepository repo;

	public Event create(EventDto dto) {
		Event e = new Event();
		e.setTitle(dto.getTitle());
		e.setDescription(dto.getDescription());
		e.setStart(dto.getStart());
		e.setEnd(dto.getEnd());

		repo.save(e);
		return e;
	}

	public Event update(UUID id, EventDto dto)
	{
		Optional<Event> op = repo.findById(id);
		if(op.isEmpty())
			throw new InvalidCredentials("Evento inesistente");

		Event e = op.get();
		e.setTitle(dto.getTitle());
		e.setDescription(dto.getDescription());
		e.setStart(dto.getStart());
		e.setEnd(dto.getEnd());

		repo.save(e);
		return e;
	}

	public void delete(UUID id) {
		repo.deleteById(id);
	}

	public List<Event> getEvents(LocalDateTime start, LocalDateTime end) {
		return repo.findEventsInRange(start, end);
	}
}
