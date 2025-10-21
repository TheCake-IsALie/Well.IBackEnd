package org.generation.wellibackend.model.dtos;

import lombok.Getter;
import lombok.Setter;
import org.generation.wellibackend.model.enums.Gender;

@Getter
@Setter
public class UserPutDto
{
	private String password;
	private Gender gender;
	private String city;
}
