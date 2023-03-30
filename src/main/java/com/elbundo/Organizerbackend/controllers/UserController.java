package com.elbundo.Organizerbackend.controllers;

import com.elbundo.Organizerbackend.dto.*;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.services.Impl.UserServiceImpl;
import com.elbundo.Organizerbackend.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"${frontend.endpoint}" }, allowCredentials = "true")
@Slf4j
public class UserController {
    private final UserService userService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserInfo>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewUserResponse> newUser(@RequestBody User user) throws Exception {
        return ResponseEntity.ok(userService.addUser(user));
    }
    @PatchMapping("/{id}/dropPassword")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DropPasswordResponse> dropPassword(@PathVariable Long id) {
        return ResponseEntity.ok(userService.dropPassword(id));
    }
    @PatchMapping("/changePassword")
    public ResponseEntity<AuthenticationResponse> changePassword(HttpServletResponse response, @RequestBody ChangePasswordRequest passwordRequest, @AuthenticationPrincipal User user) {
        TokenPair tokens = userService.changePassword(user, passwordRequest);
        Cookie cookie = new Cookie("refreshToken", tokens.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24*7);
        response.addCookie(cookie);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .accessToken(tokens.accessToken())
                .build());
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
