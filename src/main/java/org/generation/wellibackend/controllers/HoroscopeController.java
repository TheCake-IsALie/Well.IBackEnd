package org.generation.wellibackend.controllers;

import org.generation.wellibackend.model.dtos.HoroscopeResponseDto;
import org.generation.wellibackend.services.HoroscopeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/horoscope")
public class HoroscopeController {

    private final HoroscopeService svc;
    public HoroscopeController(HoroscopeService svc) { this.svc = svc; }


    @GetMapping
    public ResponseEntity<HoroscopeResponseDto> get(
            @RequestParam(value = "sign", required = false) String sign,
            @RequestParam(value = "scope", required = false, defaultValue = "daily") String scope,
            @RequestParam(value = "timezone", required = false, defaultValue = "Europe/Rome") String timezone
    ) {
        return ResponseEntity.ok(svc.getOnDemand(sign, scope, timezone));
    }
}
