package org.generation.wellibackend.model.entities;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import org.generation.wellibackend.model.enums.HoroscopeScope;

import java.time.OffsetDateTime;

@Entity
@Table(name = "horoscope_entry",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sign","scope","period_key"}))
@Getter @Setter
public class HoroscopeEntry extends BaseEntity {

    @Column(name = "sign", length = 20, nullable = false)
    private String sign;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", length = 10, nullable = false)
    private HoroscopeScope scope;

    @Column(name = "period_key", length = 16, nullable = false)
    private String periodKey;

    @Column(name = "text_work", length = 1500, nullable = false)
    private String textWork;

    @Column(name = "text_health", length = 1500, nullable = false)
    private String textHealth;

    @Column(name = "text_love", length = 1500, nullable = false)
    private String textLove;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
