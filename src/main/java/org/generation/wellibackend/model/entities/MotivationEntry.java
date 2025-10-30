package org.generation.wellibackend.model.entities;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.time.LocalDate; import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "motivation_entry",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","day"}))
@Getter @Setter
public class MotivationEntry extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "day", nullable = false)
    private LocalDate day;

    @Column(name = "text", length = 500, nullable = false)
    private String text;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
