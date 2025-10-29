// src/main/java/org/generation/wellibackend/controllers/MotivationController.java
package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.MotivationResponseDto;
import org.generation.wellibackend.services.MotivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/motivation")
public class MotivationController {

    private final MotivationService svc;
    public MotivationController(MotivationService svc) { this.svc = svc; }

    private String token(String authorization) {
        return (authorization != null && authorization.startsWith("Bearer ")) ? authorization.substring(7) : null;
    }

    @GetMapping
    public ResponseEntity<MotivationResponseDto> today(@RequestHeader("Authorization") String authorization) {
        String t = token(authorization);
        if (t == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(svc.getTodayOnDemand(t));
    }
}
