package com.elbundo.Organizerbackend.services;

import com.elbundo.Organizerbackend.dto.NewUserDTO;
import com.elbundo.Organizerbackend.models.Role;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public NewUserDTO addNewUser(User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent())
            return null;
        String password = generatePassword();
        User newUser = new User(null, user.getName(), user.getUsername(), passwordEncoder.encode(password), user.getRole(), null);
        return new NewUserDTO(userRepository.save(newUser), password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String generatePassword() {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@";
        Random rand = new Random(System.currentTimeMillis());
        int len = 8 + rand.nextInt(8);
        StringBuilder pass = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            pass.append(alphabet.charAt(rand.nextInt(alphabet.length())));
        }
        return pass.toString();
    }

    public NewUserDTO dropPassword(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())
            return null;
        String password = generatePassword();
        User usr = user.get();
        usr.setPassword(passwordEncoder.encode(password));
        return new NewUserDTO(userRepository.save(usr), password);
    }

    public void deleteUser(Long id) {
        if(!id.equals(1L))
            userRepository.deleteById(id);
    }
}
