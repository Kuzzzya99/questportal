package com.softarex.kuzmich.questportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String text) {
        super(text);
    }

    public UserAlreadyExistsException() {
        super();
    }
}
