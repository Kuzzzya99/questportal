package com.softarex.kuzmich.questportal.exception;

public class NoEntityException extends RuntimeException {
    public NoEntityException(String text) {
        super(text);
    }

    public NoEntityException() {
        super();
    }
}
