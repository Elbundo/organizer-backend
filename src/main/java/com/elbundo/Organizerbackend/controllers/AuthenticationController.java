package com.elbundo.Organizerbackend.controllers;

import com.elbundo.Organizerbackend.dto.AuthenticationRequest;
import com.elbundo.Organizerbackend.dto.AuthenticationResponse;
import com.elbundo.Organizerbackend.dto.TokenPair;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(value = "${frontend.endpoint}", allowCredentials = "true")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        TokenPair tokens = service.authenticate(request);
        Cookie cookie = new Cookie("refreshToken", tokens.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24*7);
        response.addCookie(cookie);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .accessToken(tokens.accessToken())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
                break;
            }
        }
        if (refreshToken == null)
            return new ResponseEntity<>(HttpStatusCode.valueOf(401));
        TokenPair tokens = service.refresh(refreshToken);
        Cookie cookie = new Cookie("refreshToken", tokens.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24*7);
        response.addCookie(cookie);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .accessToken(tokens.accessToken())
                .build());
    }

    @GetMapping("/logout")
    public void logout(@AuthenticationPrincipal User user) {
        service.logout(user);
    }

}
