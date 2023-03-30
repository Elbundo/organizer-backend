package com.elbundo.Organizerbackend.services;

import com.elbundo.Organizerbackend.dto.TypeTasks;
import com.elbundo.Organizerbackend.exceptions.AddTaskException;
import com.elbundo.Organizerbackend.exceptions.ChangeTaskException;
import com.elbundo.Organizerbackend.exceptions.TaskNotExistsException;
import com.elbundo.Organizerbackend.models.Task;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.repositories.TaskRepository;
import com.elbundo.Organizerbackend.services.Impl.TaskServiceImpl;
import com.elbundo.Organizerbackend.services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void shouldReturnAllTaskWhenProvideTypeIsAll() {
        User alex = User.builder().login("Alex").build();
        List<Task> alexTasks = List.of(
                Task.builder().id(1L).text("Task1").status(true).user(alex).build(),
                Task.builder().id(3L).text("Task3").status(true).user(alex).build(),
                Task.builder().id(5L).text("Task5").status(false).user(alex).build()
        );
        when(taskRepository.findAllByUser(alex))
                .thenReturn(
                        alexTasks
                );

        TaskService taskService = new TaskServiceImpl(taskRepository);

        assertEquals(taskService.getTasks(TypeTasks.ALL, alex), alexTasks);
    }

    @Test
    public void shouldReturnCompletedTaskWhenProvideTypeIsCompleted() {
        User alex = User.builder().login("Alex").build();
        List<Task> alexTasks = List.of(
                Task.builder().id(1L).text("Task1").status(true).user(alex).build(),
                Task.builder().id(3L).text("Task3").status(true).user(alex).build(),
                Task.builder().id(5L).text("Task5").status(false).user(alex).build()
        );
        when(taskRepository.findByUserAndStatus(alex, true))
                .thenReturn(
                        alexTasks.stream().filter(task -> task.getStatus().equals(true)).collect(Collectors.toList())
                );

        TaskService taskService = new TaskServiceImpl(taskRepository);

        assertEquals(taskService.getTasks(TypeTasks.COMPLETED, alex), alexTasks.stream().filter(task -> task.getStatus().equals(true)).collect(Collectors.toList()));
    }

    @Test
    public void shouldReturnNonCompletedTaskWhenProvideTypeIsNonCompleted() {
        User alex = User.builder().login("Alex").build();
        List<Task> alexTasks = List.of(
                Task.builder().id(1L).text("Task1").status(true).user(alex).build(),
                Task.builder().id(3L).text("Task3").status(true).user(alex).build(),
                Task.builder().id(5L).text("Task5").status(false).user(alex).build()
        );
        when(taskRepository.findByUserAndStatus(alex, false))
                .thenReturn(
                        alexTasks.stream().filter(task -> task.getStatus().equals(false)).collect(Collectors.toList())
                );

        TaskService taskService = new TaskServiceImpl(taskRepository);

        assertEquals(taskService.getTasks(TypeTasks.NON_COMPLETED, alex), alexTasks.stream().filter(task -> task.getStatus().equals(false)).collect(Collectors.toList()));
    }

    @Test
    public void shouldReturnCreatedTaskWhenTextIsNotNullOrEmpty() throws AddTaskException {
        User alex = User.builder().login("Alex").build();
        Task task = Task.builder().text("Task1").build();
        when(taskRepository.save(task))
                .thenReturn(
                        Task.builder().id(1L).text(task.getText()).status(false).user(alex).build()
                );

        TaskService taskService = new TaskServiceImpl(taskRepository);

        var result = taskService.addTask(task, alex);

        assertNotNull(result.getId());
        assertNotNull(result.getText());
        assertEquals(result.getText(), task.getText());
        assertNotNull(result.getStatus());
        assertFalse(result.getStatus());
        assertNotNull(result.getUser());
        assertEquals(result.getUser(), alex);
    }
    @Test
    public void shouldThrowAddTaskExceptionWhenTextIsNull() {
        User alex = User.builder().login("Alex").build();
        Task task = Task.builder().user(alex).build();

        TaskService taskService = new TaskServiceImpl(taskRepository);


        assertThrows(AddTaskException.class, () -> taskService.addTask(task, alex), "Task's text is null");
    }
    @Test
    public void shouldThrowAddTaskExceptionWhenTextIsEmpty() {
        User alex = User.builder().login("Alex").build();
        Task task = Task.builder().text("").user(alex).build();

        TaskService taskService = new TaskServiceImpl(taskRepository);

        assertThrows(AddTaskException.class, () -> taskService.addTask(task, alex), "Task's text is empty");
    }

    @Test
    public void shouldThrowTaskNotExistsExceptionWhenProvideWrongId() {
        User alex = User.builder().login("Alex").build();
        Task task = Task.builder().id(1L).text("").status(false).user(alex).build();

        TaskService taskService = new TaskServiceImpl(taskRepository);

        when(taskRepository.findById(1L))
                .thenReturn(
                        Optional.empty()
                );

        assertThrows(TaskNotExistsException.class, () -> taskService.changeTask(1L, task, alex), "Task does not exist");
    }
    @Test
    public void shouldThrowAccessDeniedExceptionWhenUserHasNoAccessToTask() {
        User alex = User.builder().login("Alex").build();
        User john = User.builder().login("John").build();
        Task task = Task.builder().id(1L).text("").status(false).user(john).build();

        TaskService taskService = new TaskServiceImpl(taskRepository);

        when(taskRepository.findById(1L))
                .thenReturn(
                        Optional.of(task)
                );

        assertThrows(AccessDeniedException.class, () -> taskService.changeTask(1L, task, alex), "User has no access to task");
    }

    @Test
    public void shouldReturnChangedTaskWhenProvideValidChanges() throws Exception {
        User alex = User.builder().login("Alex").build();
        User john = User.builder().login("John").build();
        Task task = Task.builder().id(1L).text("Do something").status(false).user(alex).build();

        Task taskChanges = Task.builder().id(2L).text("Do everything").status(true).user(john).build();
        TaskService taskService = new TaskServiceImpl(taskRepository);

        when(taskRepository.findById(1L))
                .thenReturn(
                        Optional.of(task)
                );

        when(taskRepository.save(task))
                .thenReturn(
                        task
                );


        Task newTask = taskService.changeTask(1L, taskChanges, alex);
        assertEquals(1L, newTask.getId());
        assertEquals("Do everything", newTask.getText());
        assertEquals(true, newTask.getStatus());
        assertEquals(alex, newTask.getUser());
    }
}
