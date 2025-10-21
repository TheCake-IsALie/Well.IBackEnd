package org.generation.wellibackend.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Setter
@Getter
public class Role  implements GrantedAuthority
{
	@Id
	private int id;

	private String roleName;

	@Override
	public String getAuthority() {
		return "ROLE_"+roleName.toUpperCase();
	}
}
