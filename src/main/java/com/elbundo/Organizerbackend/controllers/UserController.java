package com.elbundo.Organizerbackend.controllers;

import com.elbundo.Organizerbackend.dto.NewUserDTO;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000" }, allowCredentials = "true")
@Slf4j
public class UserController {
    private final UserService userService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers().stream().filter(user -> !user.getUsername().equals("elbundo")).toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewUserDTO> newUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addNewUser(user));
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewUserDTO> dropPassword(@PathVariable Long id) {
        return ResponseEntity.ok(userService.dropPassword(id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
