package com.elbundo.Organizerbackend.services.Impl;

import com.elbundo.Organizerbackend.dto.AuthenticationRequest;
import com.elbundo.Organizerbackend.dto.AuthenticationResponse;
import com.elbundo.Organizerbackend.dto.TokenPair;
import com.elbundo.Organizerbackend.exceptions.ExpiredRefreshTokenException;
import com.elbundo.Organizerbackend.exceptions.NotValidTokenException;
import com.elbundo.Organizerbackend.models.RefreshToken;
import com.elbundo.Organizerbackend.models.User;
import com.elbundo.Organizerbackend.repositories.TokenRepository;
import com.elbundo.Organizerbackend.repositories.UserRepository;
import com.elbundo.Organizerbackend.services.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    @Override
    public TokenPair authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );
        var user  = userRepository.findByLogin(request.getLogin()).orElseThrow();
        var tokens = jwtService.generateTokens(user);
        tokenRepository.save(RefreshToken.builder()
                .id(user.getId())
                .refreshToken(tokens.refreshToken())
                .user(user)
                .build());
        return tokens;
    }

    @Override
    public TokenPair refresh(String refreshToken) throws AuthenticationException {
        if (jwtService.isTokenExpired(refreshToken))
            throw new ExpiredRefreshTokenException("Refresh token has been expired");

        String login = jwtService.extractLogin(refreshToken);
        User user = userRepository.findByLogin(login).orElseThrow(() -> new NotValidTokenException("User not found"));
        RefreshToken refreshTokenFromDb = tokenRepository.getByUser(user).orElseThrow(() -> new NotValidTokenException("Refresh token does not belong to this user"));

        if (!refreshToken.equals(refreshTokenFromDb.getRefreshToken()))
            throw new NotValidTokenException("Not valid refresh token");

        TokenPair tokens = jwtService.generateTokens(user);
        refreshTokenFromDb.setRefreshToken(tokens.refreshToken());
        tokenRepository.save(refreshTokenFromDb);
        return tokens;
    }

    @Override
    public void logout(User user) {
        tokenRepository.deleteById(user.getId());
    }
}
