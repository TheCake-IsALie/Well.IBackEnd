package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.LifestyleAdviceDto;
import org.generation.wellibackend.model.dtos.LifestyleInputDto;
import org.generation.wellibackend.services.LifestyleAdviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wellness")
public class LifestyleAdviceController {

    private final LifestyleAdviceService service;

    public LifestyleAdviceController(LifestyleAdviceService service) {
        this.service = service;
    }

    @PostMapping("/summary")
    public ResponseEntity<LifestyleAdviceDto> summarize(@RequestBody LifestyleInputDto body) {
        return ResponseEntity.ok(service.evaluate(body));
    }
}
