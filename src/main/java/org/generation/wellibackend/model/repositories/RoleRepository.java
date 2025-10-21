package org.generation.wellibackend.model.repositories;


import org.generation.wellibackend.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>
{
	Optional<Role> findByRoleName(String name);

	@Query("SELECT r FROM Role r WHERE r.roleName='STANDARD'")
	Role getUserRole();
}