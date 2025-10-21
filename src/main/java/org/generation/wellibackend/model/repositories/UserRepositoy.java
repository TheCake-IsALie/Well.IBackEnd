package org.generation.wellibackend.model.repositories;

import org.generation.wellibackend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoy extends JpaRepository<User, UUID>
{
	Optional<User> findByEmailAndPassword(String email, String password);
	Optional<User> findByToken(String token);
	Optional<User> findByEmail(String email);
}
