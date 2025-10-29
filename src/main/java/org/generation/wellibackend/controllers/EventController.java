package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.EventDto;
import org.generation.wellibackend.model.entities.Event;
import org.generation.wellibackend.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {
	@Autowired
	private EventService service;

	@PostMapping
	public Event create(@Valid @RequestBody EventDto dto) {
		return service.create(dto);
	}

	@PutMapping("/{id}")
	public Event update(@PathVariable UUID id, @Valid @RequestBody EventDto dto) {
		return service.update(id, dto);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}

	@GetMapping
	public List<Event> getEvents(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {

		return service.getEvents(start, end);
	}
}
