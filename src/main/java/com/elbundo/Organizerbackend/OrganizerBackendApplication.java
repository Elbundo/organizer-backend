package com.elbundo.Organizerbackend;

import com.elbundo.Organizerbackend.models.Role;
import com.elbundo.Organizerbackend.models.Task;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.repositories.TaskRepository;
import com.elbundo.Organizerbackend.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class OrganizerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrganizerBackendApplication.class, args);
	}

	@Bean
	@Profile({"dev", "integrationtest"})
	@Transactional
	public CommandLineRunner runner(UserRepository userRepository, TaskRepository taskRepository, PasswordEncoder encoder) {
		return args -> {
			User admin = User.builder()
					.id(1L)
					.name("admin")
					.login("admin")
					.password(encoder.encode("admin"))
					.role(Role.ROLE_ADMIN)
					.build();
			User user = User.builder()
					.id(2L)
					.name("user")
					.login("user")
					.password(encoder.encode("user"))
					.role(Role.ROLE_USER)
					.build();
			userRepository.save(admin);
			userRepository.save(user);
			List<Task> tasks = List.of(
				Task.builder()
						.id(3L)
						.text("Do something")
						.status(false)
						.user(admin)
						.build(),
				Task.builder()
						.id(4L)
						.text("Do something else")
						.status(true)
						.user(admin)
						.build(),
				Task.builder()
						.id(5L)
						.text("Do homework")
						.status(false)
						.user(user)
						.build(),
				Task.builder()
						.id(5L)
						.text("Go to the gym")
						.status(true)
						.user(user)
						.build()
			);
			for (Task task : tasks)
				taskRepository.save(task);
		};
	}
}