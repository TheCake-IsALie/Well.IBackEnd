package org.generation.wellibackend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "mood_entry",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","day"}))
@Getter
@Setter
public class MoodEntry extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "day", nullable = false)
    private LocalDate day;

    @Column(name = "mood_text", length = 1000, nullable = false)
    private String moodText;

    @Column(name = "ai_summary", length = 1000)
    private String aiSummary;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
