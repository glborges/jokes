package com.example.jokes.adapter.joke.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalJokeApiClientResponse(String error, int amount, List<ExternalJokeApiClientJoke> jokes) {
}
