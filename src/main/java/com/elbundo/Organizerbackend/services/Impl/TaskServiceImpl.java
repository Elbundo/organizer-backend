package com.elbundo.Organizerbackend.services.Impl;

import com.elbundo.Organizerbackend.dto.TypeTasks;
import com.elbundo.Organizerbackend.exceptions.AddTaskException;
import com.elbundo.Organizerbackend.exceptions.TaskNotExistsException;
import com.elbundo.Organizerbackend.models.Task;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.repositories.TaskRepository;
import com.elbundo.Organizerbackend.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;

    @Override
    public List<Task> getTasks(TypeTasks type, User user) throws IllegalStateException {
        switch (type) {
            case ALL -> {
                return repository.findAllByUser(user);
            }
            case COMPLETED -> {
                return repository.findByUserAndStatus(user, true);
            }
            case NON_COMPLETED -> {
                return repository.findByUserAndStatus(user, false);
            }
            default -> throw new IllegalStateException("Unexpected type: " + type);
        }
    }

    @Override
    public Task addTask(Task task, User user) throws AddTaskException {
        if (task.getText() == null)
            throw new AddTaskException("Task's text is null");
        if (task.getText().isEmpty())
            throw new AddTaskException("Task's text is empty");
        task.setId(null);
        task.setUser(user);
        task.setStatus(false);
        return repository.save(task);
    }

    @Override
    public Task changeTask(Long id, Task taskChanges, User user) throws Exception {
        Task task = repository.findById(id).orElseThrow(() -> new TaskNotExistsException("Task does not exits"));
        if (!task.getUser().equals(user))
            throw new AccessDeniedException("User has no access to task");
        if (taskChanges.getText() != null && !taskChanges.getText().isBlank())
            task.setText(taskChanges.getText());
        if (taskChanges.getStatus() != null)
            task.setStatus(taskChanges.getStatus());
        return repository.save(task);
    }

    @Override
    public void deleteTask(Long id, User user) throws Exception{
        Task task = repository.findById(id).orElseThrow(() -> new TaskNotExistsException("Task does not exits"));
        if (!task.getUser().equals(user))
            throw new AccessDeniedException("Can't change task");
        repository.delete(task);
    }
}
