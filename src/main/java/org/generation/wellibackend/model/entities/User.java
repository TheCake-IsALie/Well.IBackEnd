package org.generation.wellibackend.model.entities;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.generation.wellibackend.model.enums.Gender;
import org.generation.wellibackend.utilities.ObjectNodeConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseEntity implements UserDetails
{
    @NotNull @NotBlank
    private String name, surname, password, city;

    @Column(unique = true)
    private String email;

    @NotNull
    private Gender gender;
    @NotNull
    private LocalDate dob;

    private String token;

    private boolean firstDailyAccess = true;

    @Convert(converter = ObjectNodeConverter.class)
    @Column(columnDefinition = "JSON")
    private ObjectNode extraData = JsonNodeFactory.instance.objectNode();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    private String avatarUrl;
}
