package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DayRepositoy extends JpaRepository<Day, LocalDate>
{
}
