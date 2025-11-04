package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>
{
//	//Query originale
//	@Query("SELECT e FROM Event e WHERE e.user.id = :userId AND e.start <= :end AND e.end >= :start")
//	List<Event> findEventsInRangeForUser(
//			@Param("userId") UUID userId,
//			@Param("start") LocalDateTime start,
//			@Param("end") LocalDateTime end);

	// Query correzione: Usa < e > per gestire correttamente gli intervalli esclusivi (logica "overlap")
	@Query("SELECT e FROM Event e WHERE e.user.id = :userId AND e.start < :end AND e.end > :start")
	List<Event> findEventsInRangeForUser(
			@Param("userId") UUID userId,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);

	// NUOVO METODO ESSENZIALE (per update/delete sicuri)
	Optional<Event> findByIdAndUserId(UUID eventId, UUID userId);
}
