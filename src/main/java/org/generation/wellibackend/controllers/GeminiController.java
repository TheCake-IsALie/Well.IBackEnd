package org.generation.wellibackend.controllers;

import lombok.RequiredArgsConstructor;
import org.generation.wellibackend.model.dtos.HoroscopeDto;
import org.generation.wellibackend.model.dtos.PhraseDto;
import org.generation.wellibackend.model.entities.Mood;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.model.repositories.MoodRepository;
import org.generation.wellibackend.model.repositories.UserRepository;
import org.generation.wellibackend.services.GeminiService;
import org.generation.wellibackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Import per il Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController {

	private final GeminiService geminiService;

	private static final Logger logger = LoggerFactory.getLogger(GeminiController.class);

	@Autowired
	private UserService userService; // Inietta UserService

	@Autowired
	private MoodRepository moodRepository; // Inietta MoodRepository

	@Autowired
	private UserRepository uRepo;

	@GetMapping("/horoscope")
	public ResponseEntity<?> getHoroscope(@AuthenticationPrincipal User user) {

		if (user == null) {
			logger.warn("Accesso a /horoscope senza utente autenticato.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HoroscopeDto("Errore", "Utente non autenticato."));
		}
		if (user.getDob() == null) {
			logger.warn("L'utente {} ha richiesto l'oroscopo ma non ha una data di nascita.", user.getEmail());
			return ResponseEntity.badRequest().body(new HoroscopeDto("Dati Mancanti", "Per favore, imposta la tua data di nascita nel profilo."));
		}

		LocalDate dob = user.getDob();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
		String formattedDob = dob.format(formatter);

		String prompt = String.format(
				"Agisci come un astrologo. Fornisci l'oroscopo del giorno (oggi) per una persona nata il %s. " +
						"La risposta deve essere in italiano, breve (2-3 frasi) e motivazionale. " +
						"Restituisci ESCLUSIVAMENTE il testo nel formato: Titolo Breve(SOLO segno zodiacale|Descrizione dell'oroscopo.",
				formattedDob
		);

		logger.info("Richiesta oroscopo per utente: {}", user.getEmail());

		String geminiResponse;
		try {
			geminiResponse = geminiService.getResponseGemini(prompt);

		} catch (Exception e) {
			logger.error("Errore durante la chiamata a GeminiService per l'utente {}: {}", user.getEmail(), e.getMessage(), e);

			return ResponseEntity.internalServerError().body(new HoroscopeDto("Errore Servizio", "Impossibile contattare il servizio di astrologia al momento. Riprova più tardi."));
		}

		if (geminiResponse == null || geminiResponse.isBlank()) {
			logger.warn("Gemini ha restituito una risposta nulla o vuota per l'utente {}.", user.getEmail());
			return ResponseEntity.internalServerError().body(new HoroscopeDto("Nessuna Risposta", "Il servizio di astrologia non ha fornito una risposta."));
		}

		String[] parts = geminiResponse.split("\\|");
		String title;
		String description;

		if (parts.length >= 2) {
			title = parts[0].trim().replaceAll("[\"*]", "");
			description = parts[1].trim().replaceAll("[\"*]", "");
		} else {
			logger.warn("Gemini non ha restituito il formato atteso (Titolo|Descrizione). Risposta: {}", geminiResponse);
			title = "Oroscopo del Giorno";
			description = geminiResponse.trim();
		}

		return ResponseEntity.ok(new HoroscopeDto(title, description));
	}

	@GetMapping("/quote-of-the-day")
	public ResponseEntity<PhraseDto> getQuoteOfTheDay(@AuthenticationPrincipal User user) {
		String email = user.getEmail();
		Optional<User> userOpt = uRepo.findByEmail(email);

		if (userOpt.isEmpty()) {
			return ResponseEntity.status(401).build(); // Utente non trovato
		}
		user = userOpt.get();

		Mood userMood = null;
		for(Mood m:moodRepository.findAll())
			if(user == m.getUser())
				 userMood = m;



		PhraseDto quote = geminiService.getMotivationalQuoteByMood(userMood.getMood());
		return ResponseEntity.ok(quote);
	}
}