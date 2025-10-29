package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.HoroscopeEntry;
import org.generation.wellibackend.model.enums.HoroscopeScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HoroscopeEntryRepository extends JpaRepository<HoroscopeEntry, UUID> {
    Optional<HoroscopeEntry> findBySignAndScopeAndPeriodKey(String sign, HoroscopeScope scope, String periodKey);
}
