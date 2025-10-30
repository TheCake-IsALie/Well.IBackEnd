package org.generation.wellibackend.services;

import org.generation.wellibackend.exceptions.InvalidCredentials;
import org.generation.wellibackend.model.dtos.EventDto;
import org.generation.wellibackend.model.entities.Event;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.model.enums.EventStatus;
import org.generation.wellibackend.model.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import per il logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService
{
	private static final Logger logger = LoggerFactory.getLogger(EventService.class);

	@Autowired
	private EventRepository repo;
	@Autowired
	private UserService userService; // Assicurati che questo sia iniettato

	// --- NUOVO: METODO DI MAPPING SICURO ---
	private void mapDtoToEvent(Event e, EventDto dto) {
		e.setTitle(dto.getTitle());
		e.setStart(dto.getStart());
		e.setEnd(dto.getEnd());
		e.setAllDay(dto.isAllDay());

		if (dto.getDescription() != null) e.setDescription(dto.getDescription());
		if (dto.getType() != null) e.setType(dto.getType());
		if (dto.getColor() != null) e.setColor(dto.getColor());
		if (dto.getReminderMinutes() != null) e.setReminderMinutes(dto.getReminderMinutes());

		if (dto.getShowAs() != null && !dto.getShowAs().isEmpty()) {
			try {
				e.setShowAs(EventStatus.valueOf(dto.getShowAs().toUpperCase()));
			} catch (IllegalArgumentException ex) {
				logger.warn("Valore 'showAs' non valido: {}. Uso il default.", dto.getShowAs());
				e.setShowAs(EventStatus.BUSY);
			}
		} else if (e.getShowAs() == null) {
			e.setShowAs(EventStatus.BUSY);
		}
	}

	public Event create(EventDto dto, User user) {
		logger.debug("Creazione nuovo evento per utente: {}", user.getEmail());
		Event e = new Event(); // I default (colore, showAs) sono impostati qui
		mapDtoToEvent(e, dto); // Sovrascrive i default solo se non null
		e.setUser(user);

		try {
			Event savedEvent = repo.save(e);
			logger.info("Evento creato con ID: {} per utente: {}", savedEvent.getId(), user.getEmail());
			return savedEvent;
		} catch (Exception ex) {
			logger.error("ERRORE DURANTE IL SALVATAGGIO DELL'EVENTO: {}", ex.getMessage());
			throw new InvalidCredentials("Errore nel salvataggio dell'evento: " + ex.getMessage());
		}
	}

	public Event update(UUID id, EventDto dto, User user)
	{
		logger.debug("Aggiornamento evento ID: {} per utente: {}", id, user.getEmail());
		Optional<Event> op = repo.findByIdAndUserId(id, user.getId());
		if(op.isEmpty()) {
			logger.warn("Tentativo di aggiornare evento inesistente o non autorizzato. ID: {}, Utente: {}", id, user.getEmail());
			throw new InvalidCredentials("Evento inesistente o non autorizzato");
		}

		Event e = op.get();
		mapDtoToEvent(e, dto); // Applica le modifiche
		Event updatedEvent = repo.save(e);
		logger.info("Evento aggiornato con ID: {} per utente: {}", updatedEvent.getId(), user.getEmail());
		return updatedEvent;
	}

	public void delete(UUID id, User user) {
		// ... (metodo delete invariato) ...
		logger.debug("Tentativo eliminazione evento ID: {} per utente: {}", id, user.getEmail());
		Optional<Event> op = repo.findByIdAndUserId(id, user.getId());
		if(op.isEmpty()) {
			logger.warn("Tentativo di eliminare evento inesistente o non autorizzato. ID: {}, Utente: {}", id, user.getEmail());
			throw new InvalidCredentials("Evento inesistente o non autorizzato");
		}
		repo.deleteById(id);
		logger.info("Evento eliminato con ID: {} per utente: {}", id, user.getEmail());
	}

	public List<Event> getEvents(OffsetDateTime start, OffsetDateTime end, User user) {
		logger.info("Richiesta getEvents per utente: {}", user.getEmail());

		// 1. Converti OffsetDateTime in LocalDateTime (qui nel Service!)
		LocalDateTime localStart = start.toLocalDateTime();
		LocalDateTime localEnd = end.toLocalDateTime();
		logger.debug("Convertito a LocalDateTime - localStart: {}, localEnd: {}", localStart, localEnd);

		// 2. Chiama il REPOSITORY con i tipi corretti
		List<Event> events = repo.findEventsInRangeForUser(user.getId(), localStart, localEnd);
		logger.info("Trovati {} eventi per utente {} nell'intervallo specificato.", events.size(), user.getEmail());

		return events;
	}
}