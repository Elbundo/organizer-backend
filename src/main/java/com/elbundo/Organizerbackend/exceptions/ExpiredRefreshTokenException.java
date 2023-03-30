package com.elbundo.Organizerbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredRefreshTokenException extends AuthenticationException {
    public ExpiredRefreshTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ExpiredRefreshTokenException(String msg) {
        super(msg);
    }
}
