package org.generation.wellibackend.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Richiesta per impostare il mood giornaliero.
 * Contiene una sola stringa "moodText".
 */
@Getter
@Setter
public class MoodRequestDto {

    @NotBlank(message = "moodText è obbligatorio")
    @Size(max = 500, message = "moodText non può superare 500 caratteri")
    private String moodText;
}
