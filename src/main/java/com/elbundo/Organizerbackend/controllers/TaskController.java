package com.elbundo.Organizerbackend.controllers;

import com.elbundo.Organizerbackend.dto.TypeTasks;
import com.elbundo.Organizerbackend.exceptions.AddTaskException;
import com.elbundo.Organizerbackend.models.Task;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = {"${frontend.endpoint}" }, allowCredentials = "true")
@Slf4j
public class TaskController {
    private final TaskService service;
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@AuthenticationPrincipal User user, @RequestParam TypeTasks type) {
        return ResponseEntity.ok(service.getTasks(type, user));
    }

    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task, @AuthenticationPrincipal User user) throws AddTaskException {
        return ResponseEntity.ok(service.addTask(task, user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> changeTask(@PathVariable Long id, @RequestBody Task task, @AuthenticationPrincipal User user) throws Exception {
        return ResponseEntity.ok(service.changeTask(id, task, user));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) throws Exception {
        service.deleteTask(id, user);
    }
}

