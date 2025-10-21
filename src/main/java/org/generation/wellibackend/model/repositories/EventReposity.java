package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventReposity extends JpaRepository<Event, UUID>
{
	Optional<Event> findById(UUID id);
}
