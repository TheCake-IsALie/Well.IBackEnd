package org.generation.wellibackend.model.dtos;

import lombok.Getter;
import lombok.Setter;
import org.generation.wellibackend.model.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterDto
{
	private String email;
	private String name;
	private String surname;
	private String password;
	private Gender gender;
	private LocalDate dob;
	private String city;
}
