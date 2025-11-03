package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.Mood;
import org.generation.wellibackend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MoodRepository extends JpaRepository<Mood, UUID> {
    Mood findByUser(User user);
    Mood findByUser_Token(String token);
}
