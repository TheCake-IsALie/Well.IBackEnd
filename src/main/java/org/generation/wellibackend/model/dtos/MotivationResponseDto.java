package org.generation.wellibackend.model.dtos;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter
public class MotivationResponseDto {
    private UUID userId;
    private String text;
    private LocalDate day;
}
