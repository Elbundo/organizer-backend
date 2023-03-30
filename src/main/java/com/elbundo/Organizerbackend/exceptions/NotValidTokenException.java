package com.elbundo.Organizerbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotValidTokenException extends AuthenticationException {
    public NotValidTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NotValidTokenException(String msg) {
        super(msg);
    }
}
