package com.elbundo.Organizerbackend.services;

import com.elbundo.Organizerbackend.dto.TypeTasks;
import com.elbundo.Organizerbackend.exceptions.AddTaskException;
import com.elbundo.Organizerbackend.exceptions.TaskNotExistsException;
import com.elbundo.Organizerbackend.models.Task;
import com.elbundo.Organizerbackend.models.User;

import java.util.List;

public interface TaskService {
    List<Task> getTasks(TypeTasks type, User user) throws IllegalStateException;
    Task addTask(Task task, User user) throws AddTaskException;
    Task changeTask(Long id, Task taskChanges, User user) throws Exception;
    void deleteTask(Long id, User user) throws Exception;
}
