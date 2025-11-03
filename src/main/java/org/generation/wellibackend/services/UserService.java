package org.generation.wellibackend.services;

import org.generation.wellibackend.exceptions.InvalidCredentials;
import org.generation.wellibackend.model.dtos.LoginDto;
import org.generation.wellibackend.model.dtos.RegisterDto;
import org.generation.wellibackend.model.dtos.UserDto;
import org.generation.wellibackend.model.dtos.UserPutDto;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.model.repositories.RoleRepository;
import org.generation.wellibackend.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService
{
	@Autowired
	public UserRepository uRepo;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	RoleRepository rRepo;

	private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

	public UserService() { // Costruttore per creare la directory
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	public String register(RegisterDto registerDto)
	{
		for(User u : uRepo.findAll())
		{
			if(registerDto.getEmail().equals(u.getEmail()))
				throw new InvalidCredentials("Email already used");
		}

		if(!registerDto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&-])[A-Za-z\\d@$!%*?&-]{8,}$"))
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

	public UserDto convertToUserDto(User u)
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

		dto.setAvatarUrl(u.getAvatarUrl());

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

		if(dto.getName() != null && !dto.getName().isBlank()) {
			u.setName(dto.getName());
		}
		if(dto.getSurname() != null && !dto.getSurname().isBlank()) {
			u.setSurname(dto.getSurname());
		}
		if(dto.getCity() != null && !dto.getCity().isBlank()) {
			u.setCity(dto.getCity());
		}
		if(dto.getGender() != null) {
			u.setGender(dto.getGender());
		}

		if(dto.getPassword() != null && !dto.getPassword().isBlank()) {
			u.setPassword(encoder.encode(dto.getPassword()));
		}


		uRepo.save(u);

		return u.getToken();
	}

	public String saveAvatar(User user, MultipartFile file) {

		String vecchioAvatarUrl = user.getAvatarUrl();
		if (vecchioAvatarUrl != null && !vecchioAvatarUrl.isEmpty() && vecchioAvatarUrl.startsWith("/uploads/")) {
			try {
				String nomeFileVecchio = vecchioAvatarUrl.substring("/uploads/".length());
				Path percorsoFileVecchio = this.fileStorageLocation.resolve(nomeFileVecchio).normalize();

				Files.deleteIfExists(percorsoFileVecchio);
				System.out.println("Vecchio avatar eliminato con successo: " + nomeFileVecchio);
			} catch (IOException e) {
				System.err.println("Impossibile eliminare il vecchio avatar: " + vecchioAvatarUrl + " - Errore: " + e.getMessage());
			}
		}

		String fileExtension = "";
		String originalFileName = file.getOriginalFilename();
		if (originalFileName != null && originalFileName.contains(".")) {
			fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		}
		String fileName = "user_" + user.getId() + "_" + UUID.randomUUID().toString() + fileExtension;

		try {
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			String fileUrl = "/uploads/" + fileName;

			user.setAvatarUrl(fileUrl);
			uRepo.save(user);

			return fileUrl;

		} catch (IOException ex) {
			throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public void deleteMyAccount(String token)
	{
		Optional<User> u = uRepo.findByToken(token);
		UUID id = u.get().getId();

		uRepo.deleteById(id);
	}
}
