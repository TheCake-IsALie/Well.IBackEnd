package org.generation.wellibackend.controllers;

import org.generation.wellibackend.exceptions.InvalidCredentials; // Assicurati di importare
import org.generation.wellibackend.model.dtos.EventDto;
import org.generation.wellibackend.model.entities.Event;
import org.generation.wellibackend.model.entities.User; // Importa
import org.generation.wellibackend.model.repositories.EventRepository;
import org.generation.wellibackend.model.repositories.UserRepository; // <-- IMPORTA
import org.generation.wellibackend.services.EventService;
import org.generation.wellibackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.security.Principal; // Importa
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional; // Importa
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {
	@Autowired
	private EventService service;

	@Autowired
	private UserService userService;

	// --- MODIFICA PER TEST ---
	@Autowired
	private UserRepository userRepo; // Inietta UserRepositoy

	@Autowired
	private EventRepository repo;

	// !! Modifica questa email con un utente REALE nel tuo database !!
	private static final String DEFAULT_TEST_USER_EMAIL = "test@example.com";
	// --- FINE MODIFICA ---

	// Helper per ottenere l'utente
//	private User getUserByPrincipal(Principal principal) {
//
//		// --- MODIFICA PER TEST ---
//		if (principal == null) {
//			// Se nessuno è loggato (modalità test), carica l'utente di default
//			Optional<User> op = userRepo.findByEmail(DEFAULT_TEST_USER_EMAIL);
//			if (op.isEmpty()) {
//				throw new InvalidCredentials("Utente di test non trovato! Assicurati che " +
//						DEFAULT_TEST_USER_EMAIL + " esista nel database.");
//			}
//			return op.get();
//		}
//		// --- FINE MODIFICA ---
//
//		// Logica originale
//		String email = principal.getName();
//		Optional<User> op = userRepo.findByEmail(email); // Usa userRepo
//		if (op.isEmpty()) {
//			throw new InvalidCredentials("Utente non trovato");
//		}
//		return op.get();
//	}

	// --- I METODI DEL CONTROLLER SOTTSTANTI RESTANO INVARIATI ---
	// Funzioneranno perché getUserByPrincipal(principal) ora
	// restituisce un utente fittizio invece di andare in errore.

	@PostMapping
	public Event create(@Valid @RequestBody EventDto dto, @AuthenticationPrincipal User user) {
		return service.create(dto, user);
	}

	@PutMapping("/{id}")
	public Event update(@PathVariable UUID id, @Valid @RequestBody EventDto dto, @AuthenticationPrincipal User user) {
		return service.update(id, dto, user);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id, @AuthenticationPrincipal User user) {
		service.delete(id, user);
	}

	@GetMapping
	public List<Event> getEvents(
			@RequestParam OffsetDateTime start,
			@RequestParam OffsetDateTime end,
			@AuthenticationPrincipal User user)
	{
        System.out.println(start + " " + end);
		return service.getEvents(start, end, user);
	}
}