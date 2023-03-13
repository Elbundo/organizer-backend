package com.elbundo.Organizerbackend.services;

import com.elbundo.Organizerbackend.dto.TaskDTO;
import com.elbundo.Organizerbackend.models.Task;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;

    public List<Task> getAllTasks(User user) {
        return repository.getAllTasksByUser(user);
    }

    public Task addNewTask(TaskDTO task, User user) {
        Task newTask = new Task(null, task.getText(), false, user);
        return repository.save(newTask);
    }

    public Task setTaskStatus(Long id, User user, boolean status) {
        Optional<Task> taskOpt = repository.findByIdAndUser(id, user);
        if(taskOpt.isEmpty()){
            return null;
        }
        Task task = taskOpt.get();
        task.setStatus(status);
        return repository.save(task);
    }

    public void deleteTask(Long id, User user) {
        Optional<Task> taskOpt = repository.findByIdAndUser(id, user);
        if(taskOpt.isPresent()){
            Task task = taskOpt.get();
            if(task.getUser().equals(user))
                repository.deleteById(id);
        }
    }
}
