package com.elbundo.Organizerbackend.exceptions;

public class LoginAlreadyExistException extends Exception {
    public LoginAlreadyExistException(String msg) {
        super(msg);
    }
}
