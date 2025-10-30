package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.MotivationEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface MotivationEntryRepository extends JpaRepository<MotivationEntry, UUID> {
    Optional<MotivationEntry> findByUserIdAndDay(UUID userId, LocalDate day);
}
