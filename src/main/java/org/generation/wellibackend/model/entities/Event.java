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
    private String title;

    private String description, location; //NON @NotNull perché l'utente può non avere queste info o decidere di non inserire
                                            //per la creazione dell'oggetto evento basta title, start ed end, il resto può anche essere null

    @NotNull
    private LocalDateTime start, end;

    /**
     *
     * @return String in formato "days:hours:minutes" per la durata dell'evento, se il tempo non supera le 23.59 ore return hours:minutes,
     * se il tempo non supera i 59 minuti return minutes.
     */
    public String getDuration()
    {
        Integer daysInt = (int) ChronoUnit.DAYS.between(start, end);

        Integer hoursInt = (int) ChronoUnit.HOURS.between(start, end);

        Integer minutesInt = (int) ChronoUnit.MINUTES.between(start, end);

        if(hoursInt==null)
        return minutesInt.toString();

        else if(daysInt==null)
            return hoursInt + ":" + minutesInt;

        else
        return daysInt + ":" + hoursInt + ":" + minutesInt;
    }

    /**
     *
     * @return Long minutes until the event starts.
     */
    public Long getUntilEvent()
    {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), start);
    }
}
