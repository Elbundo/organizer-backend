package com.elbundo.Organizerbackend.services;

import com.elbundo.Organizerbackend.dto.AuthenticationRequest;
import com.elbundo.Organizerbackend.dto.AuthenticationResponse;
import com.elbundo.Organizerbackend.dto.TokenPair;
import com.elbundo.Organizerbackend.models.User;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationService {
    TokenPair authenticate(AuthenticationRequest request);
    TokenPair refresh(String refreshToken) throws AuthenticationException;

    void logout(User user);
}
