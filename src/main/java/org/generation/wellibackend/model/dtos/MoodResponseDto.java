package org.generation.wellibackend.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoodResponseDto {
    private UUID userId;
    private String moodText;
    private String aiSummary;
    private LocalDate day;
}
