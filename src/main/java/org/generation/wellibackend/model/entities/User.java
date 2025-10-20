package org.generation.wellibackend.model.entities;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.generation.wellibackend.model.enums.Gender;
import org.generation.wellibackend.utilities.ObjectNodeConverter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class User extends BaseEntity {
    @NotNull @NotBlank
    private String name, surname, username, email, passwordCripted, city;
    @NotNull
    private Gender gender;
    @NotNull
    private LocalDate birthDate;
    private boolean firstDailyAccess = true;

    @Convert(converter = ObjectNodeConverter.class)
    @Column(columnDefinition = "JSON")
    private ObjectNode extraData = JsonNodeFactory.instance.objectNode();

}
