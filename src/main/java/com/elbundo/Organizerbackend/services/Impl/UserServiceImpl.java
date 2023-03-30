package com.elbundo.Organizerbackend.services.Impl;

import com.elbundo.Organizerbackend.dto.*;
import com.elbundo.Organizerbackend.exceptions.LoginAlreadyExistException;
import com.elbundo.Organizerbackend.models.RefreshToken;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.repositories.TaskRepository;
import com.elbundo.Organizerbackend.repositories.TokenRepository;
import com.elbundo.Organizerbackend.repositories.UserRepository;
import com.elbundo.Organizerbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final TokenRepository tokenRepository;

    @Override
    public List<UserInfo> getAllUser() {
        return userRepository.findAll().stream()
                .map(user -> UserInfo.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .login(user.getLogin())
                        .role(user.getRole())
                        .countTasks(taskRepository.countByUser(user))
                        .build()).collect(Collectors.toList());
    }

    @Override
    public NewUserResponse addUser(User user) throws Exception {
        if(!userRepository.existsByLogin(user.getUsername()))
            throw new LoginAlreadyExistException("User with same login already exists");
        String password = generatePassword();
        User newUser = User.builder()
                .name(user.getName())
                .login(user.getUsername())
                .password(passwordEncoder.encode(password))
                .role(user.getRole())
                .build();
        return new NewUserResponse(userRepository.save(newUser), password);
    }

    @Override
    public DropPasswordResponse dropPassword(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String password = generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return new DropPasswordResponse(password);
    }

    @Override
    public TokenPair changePassword(User user, ChangePasswordRequest request) throws BadCredentialsException {
        if (!user.getPassword().equals(request.getOldPassword()))
            throw new BadCredentialsException("Wrong password");
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        var tokens = jwtService.generateTokens(user);
        tokenRepository.save(RefreshToken.builder()
                .refreshToken(tokens.refreshToken())
                .user(user)
                .build());
        return tokens;
    }
    @Override
    public void deleteUser(Long id) {
        if(!id.equals(1L))
            userRepository.deleteById(id);
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
}
