package com.elbundo.Organizerbackend.controllers;

import com.elbundo.Organizerbackend.dto.AuthenticationRequest;
import com.elbundo.Organizerbackend.dto.AuthenticationResponse;
import com.elbundo.Organizerbackend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }

}
