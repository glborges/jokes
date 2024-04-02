package com.example.jokes.adapter.joke;

import com.example.jokes.model.DomainJoke;

import java.util.List;

public interface ExternalJokeAdapter {
    List<DomainJoke> getJokes();
}
