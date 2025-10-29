package org.generation.wellibackend.services;

import org.generation.wellibackend.exceptions.InvalidCredentials;
import org.generation.wellibackend.model.dtos.LoginDto;
import org.generation.wellibackend.model.dtos.RegisterDto;
import org.generation.wellibackend.model.dtos.UserDto;
import org.generation.wellibackend.model.dtos.UserPutDto;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.model.repositories.RoleRepository;
import org.generation.wellibackend.model.repositories.UserRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService
{
	@Autowired
	public UserRepositoy uRepo;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	RoleRepository rRepo;

	public String register(RegisterDto registerDto)
	{
		for(User u : uRepo.findAll())
		{
			if(registerDto.getEmail().equals(u.getEmail()))
				throw new InvalidCredentials("Email already used");
		}

		if(!registerDto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"))
			throw new InvalidCredentials("Password not valid");

		User user = new User();
		user.setEmail(registerDto.getEmail());
		String hash = encoder.encode(registerDto.getPassword());
		user.setPassword(hash);
		user.setName(registerDto.getName());
		user.setSurname(registerDto.getSurname());
		user.setGender(registerDto.getGender());
		user.setDob(registerDto.getDob());
		user.setCity(registerDto.getCity());
		user.setRoles(List.of(rRepo.getUserRole()));

		//genero un token in automatico
		user.setToken(UUID.randomUUID().toString());

		uRepo.save(user);

		return user.getToken();
	}

	public String login(LoginDto dto)
	{
		Optional<User> op = uRepo.findByEmail(dto.getEmail());
		if(op.isEmpty())
			throw new InvalidCredentials("Invalid email");

		if(!encoder.matches(dto.getPassword(), op.get().getPassword()))
			throw new InvalidCredentials("Invalid password");

		return op.get().getToken();
	}

	public User findUserByToken(String token)
	{
		Optional<User> op = uRepo.findByToken(token);

		if(op.isEmpty())
			throw new InvalidCredentials("Invalid token");

		return op.get();
	}

	public UserDto readUserDto(String token)
	{
		User u = findUserByToken(token);
		UserDto dto = convertToUserDto(u);
		return dto;
	}


	public UserDto readUserInfo(String token)
	{
		User u = findUserByToken(token);

		UserDto dto = convertToUserDto(u);

		return dto;
	}

	private static UserDto convertToUserDto(User u)
	{
		UserDto dto = new UserDto();
		dto.setEmail(u.getEmail());
		dto.setName(u.getName());
		dto.setSurname(u.getSurname());
		dto.setPassword(u.getPassword());
		dto.setGender(u.getGender());
		dto.setDob(u.getDob());
		dto.setCity(u.getCity());
		dto.setRoles(u.getRoles().stream().map(r-> r.getRoleName()).toList());
		return dto;
	}


	public void deleteUser(User u)
	{
		Optional<User> userOp = uRepo.findByEmail(u.getEmail());

		if(userOp.isEmpty())
			throw new InvalidCredentials("There is no such user");

		uRepo.delete(userOp.get());
	}


	public String modifyUser(UserPutDto dto, String token)
	{
		Optional<User> userOp = uRepo.findByToken(token);

		if(userOp.isEmpty())
			throw new InvalidCredentials("There is no such user");

		User u = userOp.get();
		u.setPassword(encoder.encode(dto.getPassword()));
		u.setGender(dto.getGender());
		u.setCity(dto.getCity());
		u.setToken(UUID.randomUUID().toString());

		uRepo.save(u);

		return u.getToken();
	}
}
