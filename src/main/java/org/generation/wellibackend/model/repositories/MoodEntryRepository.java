package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface MoodEntryRepository extends JpaRepository<MoodEntry, UUID> {
    Optional<MoodEntry> findByUserIdAndDay(UUID userId, LocalDate day);
    long deleteByDayBefore(LocalDate cutoff);
}
