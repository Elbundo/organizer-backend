package com.elbundo.Organizerbackend.repositories;

import com.elbundo.Organizerbackend.models.Task;
import com.elbundo.Organizerbackend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAllByUser(User user);
    List<Task> findByUserAndStatus(User user, boolean status);
    void deleteByIdAndUser(Long id, User user);
    long countByUser(User user);
}
