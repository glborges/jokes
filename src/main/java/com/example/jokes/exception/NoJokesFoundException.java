package com.example.jokes.exception;

public class NoJokesFoundException extends RuntimeException {
    public NoJokesFoundException(String message) {
        super(message);
    }
}
