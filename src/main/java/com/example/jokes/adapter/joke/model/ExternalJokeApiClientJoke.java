package com.example.jokes.adapter.joke.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalJokeApiClientJoke(int id, String type, String joke, Map<String, Boolean> flags, boolean safe) {
}