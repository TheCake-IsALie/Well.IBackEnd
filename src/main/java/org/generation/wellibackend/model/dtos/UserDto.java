package org.generation.wellibackend.model.dtos;

import lombok.Getter;
import lombok.Setter;
import org.generation.wellibackend.model.enums.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserDto
{
	private String email;
	private String name;
	private String surname;
	private String password;
	private Gender gender;
	private LocalDate dob;
	private String city;
	private List<String> roles;
	private String avatarUrl;
	private boolean firstDailyAccess;
}
