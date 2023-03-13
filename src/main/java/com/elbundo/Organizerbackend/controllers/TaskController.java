package com.elbundo.Organizerbackend.controllers;

import com.elbundo.Organizerbackend.dto.TaskDTO;
import com.elbundo.Organizerbackend.models.Task;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000" }, allowCredentials = "true")
@Slf4j
public class TaskController {
    private final TaskService service;
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.getAllTasks(user));
    }

    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody TaskDTO task, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.addNewTask(task, user));
    }

    @PutMapping
    public ResponseEntity<Task> changeStatusTask(@RequestBody TaskDTO task, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.setTaskStatus(task.getId(), user, task.isStatus()));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.deleteTask(id, user);
    }
}

