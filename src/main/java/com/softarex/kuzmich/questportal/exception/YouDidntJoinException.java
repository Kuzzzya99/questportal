package com.softarex.kuzmich.questportal.exception;

public class YouDidntJoinException extends RuntimeException {
    public YouDidntJoinException(String text) {
        super(text);
    }

    public YouDidntJoinException() {
        super();
    }
}
