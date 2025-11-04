package org.generation.wellibackend.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.generation.wellibackend.model.dtos.*;
import org.generation.wellibackend.model.entities.Mood;
import org.generation.wellibackend.model.entities.MotivationalPhrase;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.services.PhraseService;
import org.generation.wellibackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PhraseService phraseService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto, HttpServletResponse response)
    {
        String tokenUtente = userService.register(dto);

        Cookie cookie = new Cookie("token", tokenUtente);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("token", tokenUtente));
    }
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto, HttpServletResponse response)
    {
        String tokenUtente = userService.login(dto);

        Cookie cookie = new Cookie("token", tokenUtente);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
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

    @PostMapping("/mood")
    public ResponseEntity<?> setMood(@AuthenticationPrincipal User user,
                                     @RequestBody MoodDto moodDto) {
        userService.setMood(user, moodDto.getMood());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mood")
    public ResponseEntity<?> getMood(@AuthenticationPrincipal User user) {
        Mood mood = userService.getMood(user);
        if (mood == null) {
            return ResponseEntity.noContent().build(); // 204 se non c’è ancora un mood
        }
        return ResponseEntity.ok(mood.getMood());
    }

    @PostMapping("/phrase")
    public ResponseEntity<?> setPhrase(@AuthenticationPrincipal User user) {
        phraseService.setPhraseAndAuthor(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/phrase")
    public ResponseEntity<?> getPhrase(@AuthenticationPrincipal User user) {
        MotivationalPhrase phrase = phraseService.getPhraseAndAuthor(user);
        if (phrase == null) {
            return ResponseEntity.noContent().build(); // 204 se non c’è ancora un mood
        }
        PhraseDto dto= new PhraseDto();
        dto.setPhrase(phrase.getPhrase());
        dto.setAuthor(phrase.getAuthor());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal User user, @RequestBody UserPutDto dto) {
        String updatedToken = userService.modifyUser(dto, user.getToken());

        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@AuthenticationPrincipal User user, @RequestParam("profileImage") MultipartFile file) {
        try {
            String fileUrl = userService.saveAvatar(user, file);
            return ResponseEntity.ok(Map.of("message", "Avatar updated successfully", "avatarUrl", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Could not upload the file: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public void deleteMyAccount(@AuthenticationPrincipal User user)
    {
        String token = user.getToken();
        userService.deleteMyAccount(token);
    }
}