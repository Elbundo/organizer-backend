package com.elbundo.Organizerbackend;

import com.elbundo.Organizerbackend.models.Role;
import com.elbundo.Organizerbackend.models.Task;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootApplication
public class OrganizerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrganizerBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(UserRepository repository, PasswordEncoder encoder) {
		return args -> {
			User user = new User(1L, "Yaroslav", "elbundo", encoder.encode("1234"),Role.ROLE_ADMIN,null);
			Optional<User> userOpt = repository.findByUsername("elbundo");
			if(userOpt.isEmpty())
				repository.save(user);
		};
	}
}