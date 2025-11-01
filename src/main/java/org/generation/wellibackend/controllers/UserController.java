package org.generation.wellibackend.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.generation.wellibackend.model.dtos.LoginDto;
import org.generation.wellibackend.model.dtos.RegisterDto;
import org.generation.wellibackend.model.dtos.UserDto;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto, HttpServletResponse response)
    {
        String tokenUtente = userService.register(dto);

        Cookie cookie = new Cookie("token", tokenUtente);
        cookie.setMaxAge(3600);
        cookie.setPath("/api");
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("token", tokenUtente));
    }
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto, HttpServletResponse response)
    {
        String tokenUtente = userService.login(dto);

        Cookie cookie = new Cookie("token", tokenUtente);
        cookie.setMaxAge(3600);
        cookie.setPath("/api");
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("token", tokenUtente));
    }


    @GetMapping("/userinformation")
    public UserDto getUserInfo(@AuthenticationPrincipal User user)
    {
        return userService.convertToUserDto(user);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String gestisciTutto(Exception e)
    {
        return "Operazione fallita, ulteriori dettagli "+e.getMessage();
    }
}