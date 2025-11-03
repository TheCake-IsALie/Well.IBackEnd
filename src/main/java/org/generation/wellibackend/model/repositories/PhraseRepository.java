package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.MotivationalPhrase;
import org.generation.wellibackend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhraseRepository extends JpaRepository<MotivationalPhrase, UUID>
{
	MotivationalPhrase findByUser(User user);
}
