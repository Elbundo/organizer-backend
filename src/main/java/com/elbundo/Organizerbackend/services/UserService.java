package com.elbundo.Organizerbackend.services;

import com.elbundo.Organizerbackend.dto.*;
import com.elbundo.Organizerbackend.models.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    List<UserInfo> getAllUser();
    NewUserResponse addUser(User user) throws Exception;
    DropPasswordResponse dropPassword(Long id) throws UsernameNotFoundException;
    TokenPair changePassword(User user, ChangePasswordRequest request) throws BadCredentialsException;
    void deleteUser(Long id);
}
