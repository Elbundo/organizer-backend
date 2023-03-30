package com.elbundo.Organizerbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AddTaskException extends Exception {
    public AddTaskException(String msg) {
        super(msg);
    }
}