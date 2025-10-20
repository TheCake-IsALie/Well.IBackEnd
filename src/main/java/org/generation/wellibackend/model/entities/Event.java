package org.generation.wellibackend.model.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
public class Event extends BaseEntity {
    @NotNull
    private String title, description, location;
    @NotNull
    private LocalDateTime start, end;

    /**
     *
     * @return String in formato "days:hours:minutes" per la durata dell'evento
     */
    public String getDuration() {
        int daysInt = (int) java.time.Duration.between(start, end).toDays();

        int hoursInt = (int) java.time.Duration.between(start, end).toHours();

        int minutesInt = (int) java.time.Duration.between(start, end).toMinutes();

        return daysInt + ":" + hoursInt + ":" + minutesInt;
    }

    /**
     *
     * @return Long minutes until the event starts
     */
    public Long getUntilEvent() {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), start);
    }
}
