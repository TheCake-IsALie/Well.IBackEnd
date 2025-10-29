package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>
{
	/**
	 * Trova eventi la cui finestra temporale si sovrappone a [rangeStart, rangeEnd]
	 */
	@Query("SELECT e FROM Event e WHERE e.start <= :end AND e.end >= :start")
	List<Event> findEventsInRange(
			@Param("rangeStart") LocalDateTime start,
			@Param("rangeEnd") LocalDateTime end);

}
