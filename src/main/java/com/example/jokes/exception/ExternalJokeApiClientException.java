package com.example.jokes.exception;

public class ExternalJokeApiClientException extends RuntimeException {
    public ExternalJokeApiClientException(String message) {
        super(message);
    }
}
